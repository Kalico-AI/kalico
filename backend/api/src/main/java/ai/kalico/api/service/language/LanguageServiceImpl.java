package ai.kalico.api.service.language;

import ai.kalico.api.RootConfiguration;
import ai.kalico.api.data.postgres.entity.MediaContentEntity;
import ai.kalico.api.data.postgres.entity.ProjectEntity;
import ai.kalico.api.data.postgres.repo.MediaContentRepo;
import ai.kalico.api.data.postgres.repo.ProjectRepo;
import ai.kalico.api.props.OpenAiProps;
import ai.kalico.api.service.openai.OpenAiService;
import ai.kalico.api.service.openai.completion.CompletionChoice;
import ai.kalico.api.service.openai.completion.CompletionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.ContentItem;
import com.kalico.model.ContentItemChildren;
import com.kalico.model.KalicoContentType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse created on 1/30/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
  private final MediaContentRepo mediaContentRepo;
  private final ProjectRepo projectRepo;
  private final OpenAiProps openAiProps;
  private OpenAiService openAiService;
  private final ObjectMapper objectMapper;

  @PostConstruct
  public void onStart() {
    openAiService = new OpenAiService(openAiProps.getApiKey(),
        openAiProps.getRequestTimeoutSeconds());
  }

  @Override
  public List<ContentItem> generateContent(Long projectId) {
    MediaContentEntity contentEntity = mediaContentRepo.findByProjectId(projectId);
    if (contentEntity != null && contentEntity.getRawTranscript().length() > 0) {
      log.info("LanguageServiceImpl.generateContent Starting content generation for projectId={}", projectId);
      KalicoContentType contentType = KalicoContentType.OTHER;
      Optional<ProjectEntity> projectEntityOpt = projectRepo.findById(contentEntity.getProjectId());
      String description = contentEntity.getScrapedDescription();
      if (projectEntityOpt.isPresent()) {
        contentType = KalicoContentType.fromValue(projectEntityOpt.get().getContentType());
      }

      if (description != null && description.length() > openAiProps.getDescriptionCharacterLimit()) {
        // Extract details from the description
        // TODO: consolidate the description and the transcript
      }

      // Break input transcript into clusters
      List<String> chunkedTranscript = chunkTranscript(contentEntity.getRawTranscript(),
          openAiProps.getChunkSize());
      log.info("LanguageServiceImpl.generateContent Clustering transcript for projectId={}", projectId);
      List<GptResponse> clusters = clusterTranscript(chunkedTranscript);

      // Each cluster is broken down into paragraphs and the grammar and writing style is
      // improved. This acts as a section in the final output with its own subheading.
      // Subheadings are needed for longer texts, depending on the niche.
      // TODO: We can correlate the paragraphs back to the timestamped transcript to determine
      //      the best image or GIF for it automatically
      log.info("LanguageServiceImpl.generateContent Generating paragraphs for projectId={}", projectId);
      List<GptResponse> paragraphsByCluster = clustersIntoParagraphs(clusters);

      // Generate a title using the first chunk of the transcript. This eats into the token usage
      // because who have to send the entire chunk for context.
      // TODO: Design a better prompt to generate the title and do the cluster at the same time
      log.info("LanguageServiceImpl.generateContent Generating title for projectId={}", projectId);
      GptResponse title = gptCompletion(0, openAiProps.getPromptTitle(),
          chunkedTranscript.get(0));


      GptResponse recipe = null;
      if (contentType != null && contentType.equals(KalicoContentType.FOOD_RECIPE)) {
        // Extract recipe information
        // TODO: Not sure how to reconcile food recipes where the detail is spread across multiple chunks
        log.info("LanguageServiceImpl.generateContent Generating recipe for projectId={}", projectId);
        recipe = gptCompletion(0, openAiProps.getPromptRecipe(),
            chunkedTranscript.get(0));
      }

      // For FoodBlog type, do the following:
      // TODO: Extract the ingredients with quantity
      // TODO: Extract recipe steps
      log.info("LanguageServiceImpl.generateContent Generating structured content projectId={}", projectId);
      List<ContentItem> content = generateContent(title, paragraphsByCluster, recipe);
      saveContent(contentEntity.getProjectId(), content);
      log.info("LanguageServiceImpl.generateContent Finished content generation for projectId={}", projectId);
      return content;
    }
    log.info("LanguageServiceImpl.generateContent Failed to start content generation for projectId={}", projectId);
    return new ArrayList<>();
  }

  private List<ContentItem> generateContent(GptResponse title, List<GptResponse> paragraphsByCluster,
      GptResponse recipe) {
    List<ContentItem> content = new ArrayList<>();
    ContentItem item = new ContentItem()
        .type("title")
        .children(List.of(new ContentItemChildren()
            .text(cleanup(title.completionChoices.get(0).getText()))));
    content.add(item);
    for (GptResponse paragraphs : paragraphsByCluster) {
      // TODO: This is where we insert the section headers
      for (CompletionChoice choice : paragraphs.completionChoices) {
        List<String> eachParagraph = new ArrayList<>(List.of(choice.getText().split("\n")));
        for (String p : eachParagraph) {
          // Cleanup each paragraph and populate the content data structure
          String cleanedParagraph = cleanup(p);
          if (!ObjectUtils.isEmpty(cleanedParagraph)) {
            ContentItem pItem = new ContentItem()
                .type("paragraph")
                .children(List.of(new ContentItemChildren()
                    .text(cleanedParagraph)));
            content.add(pItem);
          }
        }
      }
    }
    content.addAll(generateRecipe(recipe));

    return content;
  }

  private List<ContentItem> generateRecipe(GptResponse recipe) {
    List<ContentItem> content = new ArrayList<>();
    if (recipe != null) {
      List<String> recipeDetails = Stream.of(recipe.getCompletionChoices().get(0)
              .getText().split("\n"))
          .filter(text -> !ObjectUtils.isEmpty(cleanup(text))).collect(Collectors.toList());
      // Ingredients start with a hyphen and steps start with a number
      List<String> ingredients = recipeDetails
          .stream()
          .filter(it -> it.startsWith("-")).
          map(it -> it.replace("-", "")).collect(
              Collectors.toList());
      List<String> steps = recipeDetails
          .stream()
          .filter(this::startsWithNumber)
          .map(it -> it + "\n")
          .collect(Collectors.toList());

      content.add( new ContentItem()
          .type("heading")
          .children(List.of(new ContentItemChildren()
              .text("Ingredients"))));

      content.addAll(ingredients
          .stream()
          .map(it -> new ContentItem()
              .type("check-list-item")
              .checked(false)
              .children(List.of(new ContentItemChildren()
                  .text(it)))).
          collect(Collectors.toList()));

      content.add( new ContentItem()
          .type("heading")
          .children(List.of(new ContentItemChildren()
              .text("Recipe Steps"))));

      content.add( new ContentItem()
          .type("paragraph")
          .children(steps
              .stream()
              .map(it -> new ContentItemChildren()
                  .text(it))
              .collect(Collectors.toList())));
    }
    return content;
  }

  private boolean startsWithNumber(String input) {
    String pattern = "(^\\d)";
    Pattern r = Pattern.compile(pattern);
    Matcher m = r.matcher(input);
    return m.find();
  }


  private List<GptResponse> clustersIntoParagraphs(List<GptResponse> clusters) {
    List<CompletableFuture<GptResponse>> tasks = new ArrayList<>();
    for (int i = 0; i < clusters.size(); i++) {
      final String context = extractGptResponse(clusters.get(i).getCompletionChoices());
      final int sortingOrder = i;
      tasks.add(CompletableFuture
          .supplyAsync(() -> gptCompletion(sortingOrder, openAiProps.getPromptParagraph(), context),
              RootConfiguration.executor));
    }
    return await(tasks);
  }


  private List<GptResponse> clusterTranscript(List<String> chunkedTranscript) {
    List<CompletableFuture<GptResponse>> tasks = new ArrayList<>();
    for (int i = 0; i < chunkedTranscript.size(); i++) {
      final int soringOrder = i;
      final String chunk = chunkedTranscript.get(i);
      tasks.add(CompletableFuture
          .supplyAsync(() -> gptCompletion(soringOrder, openAiProps.getPromptCluster(), chunk),
              RootConfiguration.executor));
    }
    return await(tasks);
  }

  private  GptResponse gptCompletion(int sortOrder, String prompt, String context) {
    String query = String.format("%s\n%s", prompt, cleanup(context));
    CompletionRequest completionRequest = CompletionRequest.builder()
        .model(openAiProps.getModel())
        .prompt(query)
        .echo(false)
        .temperature(openAiProps.getTemp())
        .n(1)
        .maxTokens(openAiProps.getMaxTokens())
        .user(openAiProps.getUser())
        .logitBias(new HashMap<>())
        .build();
    return new GptResponse(sortOrder, openAiService.createCompletion(completionRequest).getChoices());
  }

  private List<GptResponse> await(List<CompletableFuture<GptResponse>> tasks) {
    return tasks
        .stream()
        .map(CompletableFuture::join)
        .sorted(Comparator.comparing(GptResponse::getSortOrder))
        .collect(Collectors.toList());
  }

  @Override
  public String cleanup(String input) {
    return input
        .replace("\"", "")
        .replace("\n", " ")
        .trim();
//    String pattern = "(?:^||\\. )(?=[a-zA-Z0-9])(.*\\.)";
//    Pattern r = Pattern.compile(pattern);
//    Matcher m = r.matcher(input);
//    if (m.find()) {
//      String clean = m.group(0);
//      log.info("Output: {}", clean);
//      return clean;
//    }
//    return input;
  }

  @Override
  public List<String> chunkTranscript(String input, int chunkSize) {
    List<String> chunks = new ArrayList<>();
    StringTokenizer tokenizer = new StringTokenizer(input);
    int i = 0;
    StringJoiner joiner = new StringJoiner(" ");
    while (tokenizer.hasMoreTokens()) {
      joiner.add(tokenizer.nextToken());
      i++;
      if (i == chunkSize) {
        i = 0;
        chunks.add(joiner.toString());
        joiner = new StringJoiner(" ");
      }
    }
    if (!ObjectUtils.isEmpty(joiner.toString())) {
      chunks.add(joiner.toString());
    }
    return chunks;
  }

  @Override
  public String extractGptResponse(List<CompletionChoice> completionChoices) {
    StringJoiner joiner = new StringJoiner(" ");
    // Remove any prefixes separated by a colon
    for (CompletionChoice choice : completionChoices) {
      String text = choice.getText();
      int index = text.indexOf(":");
      if (index > 0 && index+1 < text.length()) {
        text = cleanup(text.substring(index+1));
      }
      joiner.add(text);
    }
    return joiner.toString();
  }

  private void saveContent(Long projectId, List<ContentItem> generatedContent) {
    // Update the completion status
    Optional<ProjectEntity> projectEntityOpt = projectRepo.findById(projectId);
    if (projectEntityOpt.isPresent()) {
      try {
        projectEntityOpt.get().setContent(objectMapper.writeValueAsString(generatedContent));
        projectEntityOpt.get().setProcessed(true);
        projectRepo.save(projectEntityOpt.get());
      } catch (JsonProcessingException e) {
        log.error("LanguageServiceImpl.saveContent: {}", e.getLocalizedMessage());
      }
    }
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @RequiredArgsConstructor
  private static class GptResponse {
    private int sortOrder;
    private List<CompletionChoice> completionChoices;
  }
}
