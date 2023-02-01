package ai.kalico.api.service.language;

import ai.kalico.api.data.postgres.entity.MediaContentEntity;
import ai.kalico.api.data.postgres.entity.ProjectEntity;
import ai.kalico.api.data.postgres.repo.MediaContentRepo;
import ai.kalico.api.data.postgres.repo.ProjectRepo;
import ai.kalico.api.props.OpenAiProps;
import ai.kalico.api.service.openai.OpenAiService;
import ai.kalico.api.service.openai.completion.CompletionChoice;
import ai.kalico.api.service.openai.completion.CompletionRequest;
import com.kalico.model.ContentItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
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

  @Override
  public void generateContent(String mediaId) {
    MediaContentEntity contentEntity = mediaContentRepo.findByMediaId(mediaId);
    if (contentEntity != null) {
      OpenAiService openAiService = new OpenAiService(openAiProps.getApiKey(), 60);
      Optional<ProjectEntity> projectEntityOpt = projectRepo.findById(contentEntity.getProjectId());
      String title = cleanup(contentEntity.getScrapedTitle());
      List<String> description = chunkTranscript(cleanup(contentEntity.getScrapedDescription()),
          openAiProps.getChunkSize());
//      String onScreenText = cleanup(contentEntity.getOnScreenText());
      List<String> transcript = chunkTranscript(cleanup(contentEntity.getRawTranscript()),
          openAiProps.getChunkSize());

      for (String s : transcript) {
        String prompt = String.format("%s\n%s", openAiProps.getPromptCluster(), s);
        List<CompletionChoice> choices = gptCompletion(prompt, openAiService);
        int x = 1;
      }



    }
  }

  private  List<CompletionChoice> gptCompletion(String prompt, OpenAiService openAiService) {
    CompletionRequest completionRequest = CompletionRequest.builder()
        .model(openAiProps.getModel())
        .prompt(prompt)
        .echo(false)
        .temperature(openAiProps.getTemp())
        .n(1)
        .maxTokens(openAiProps.getMaxTokens())
        .user(openAiProps.getUser())
        .logitBias(new HashMap<>())
        .build();
    return openAiService.createCompletion(completionRequest).getChoices();
  }

  @Override
  public String cleanup(String input) {
    //
    return input;
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

  private void saveContent(Long projectId, List<ContentItem> generatedContent) {
    // Update the completion status
    Optional<ProjectEntity> projectEntityOpt = projectRepo.findById(projectId);
    if (projectEntityOpt.isPresent()) {
      projectEntityOpt.get().setProcessed(true);
      projectRepo.save(projectEntityOpt.get());
    }
  }
}
