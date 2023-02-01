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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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
    openAiService = new OpenAiService(openAiProps.getApiKey(), 120);
  }

  @Override
  public List<ContentItem> generateContent(String mediaId) {
    MediaContentEntity contentEntity = mediaContentRepo.findByMediaId(mediaId);
    if (contentEntity != null && contentEntity.getRawTranscript().length() > 0) {
      Optional<ProjectEntity> projectEntityOpt = projectRepo.findById(contentEntity.getProjectId());
//      List<String> description = chunkTranscript(contentEntity.getScrapedDescription(),
//          openAiProps.getChunkSize());

      // Break input transcript into clusters
      List<String> chunkedTranscript = chunkTranscript(contentEntity.getRawTranscript(),
          openAiProps.getChunkSize());
      List<GptResponse> clusters = clusterTranscript(chunkedTranscript);

      // Each cluster is broken down into paragraphs and the grammar and writing style is
      // improved. This acts as a section in the final output with its own subheading.
      // Subheadings are needed for longer texts, depending on the niche.
      // TODO: We can correlate the paragraphs back to the timestamped transcript to determine
      //      the best image or GIF for it automatically
      List<GptResponse> paragraphsByCluster = clustersIntoParagraphs(clusters);

      // Generate a title using the first chunk of the transcript. This eats into the token usage
      // because who have to send the entire chunk for context.
      // TODO: Design a better prompt to generate the title and do the cluster at the same time
      GptResponse title = gptCompletion(0, openAiProps.getPromptTitle(), chunkedTranscript.get(0));

      // For FoodBlog type, do the following:
      // TODO: Extract the ingredients with quantity
      // TODO: Extract recipe steps
      List<ContentItem> content = generateContent(title, paragraphsByCluster);
      saveContent(contentEntity.getProjectId(), content);
      return content;
    }
    return new ArrayList<>();
  }

  private List<ContentItem> generateContent(GptResponse title, List<GptResponse> paragraphsByCluster) {
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
    return content;
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
