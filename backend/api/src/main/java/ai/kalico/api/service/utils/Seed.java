package ai.kalico.api.service.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ai.kalico.api.data.postgres.entity.ProjectEntity;
import ai.kalico.api.data.postgres.entity.SampledImageEntity;
import ai.kalico.api.data.postgres.entity.UserEntity;
import ai.kalico.api.data.postgres.entity.MediaContentEntity;
import ai.kalico.api.data.postgres.repo.ProjectRepo;
import ai.kalico.api.data.postgres.repo.SampledImageRepo;
import ai.kalico.api.data.postgres.repo.UserRepo;
import ai.kalico.api.data.postgres.repo.MediaContentRepo;
import ai.kalico.api.props.AWSProps;
import ai.kalico.api.props.ProjectProps;
import com.kalico.model.ContentItem;
import com.kalico.model.ContentItemChildren;
import com.kalico.model.KalicoContentType;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

/**
 * @author Biz Melesse created on 1/29/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Seed {
  private final ProjectRepo projectRepo;
  private final MediaContentRepo mediaContentRepo;
  private final SampledImageRepo sampledImageRepo;
  private final UserRepo userRepo;
  private final ProjectProps projectProps;
  private final ObjectMapper objectMapper;
  private final AWSProps awsProps;

  public void seed() {
    if (projectProps.getSeedDb() != null && projectProps.getSeedDb()) {
      int numRecords = 5;
      if (projectRepo.count() != numRecords) {
        try {
          String userId = createUserAccounts();
          List<ProjectEntity> projectEntities = createProject(numRecords, userId);
          
          List<Long> projectIds = projectEntities.stream()
              .map(ProjectEntity::getId)
              .collect(Collectors.toList());
          createSampledImages(projectIds);
          createMediaContent(projectIds);
        } catch (Exception e) {
          log.error(e.getLocalizedMessage());
        }
      }
    }
  }

  @SneakyThrows
  private String createUserAccounts() {
    TypeReference<List<UserEntity>> typeRef = new TypeReference<>() {};
    List<UserEntity> userEntities = objectMapper.readValue(loadFromFile("mr_public_user.json"), typeRef);
    userRepo.saveAll(userEntities);
    return userEntities.get(1).getFirebaseId();
  }
  

  @SneakyThrows
  private List<ProjectEntity> createProject(int count, String userId) {
    List<ProjectEntity> projectEntities = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      ProjectEntity entity = new ProjectEntity();
      List<ContentItem> content = new ArrayList<>();
      content.add(new ContentItem()
          .type("title")
          .children(List.of(new ContentItemChildren()
              .text("Hello, World!"))));
      entity.setContent(objectMapper.writeValueAsString(content));
      entity.setContentLink("https://www.instagram.com/p/CmGPqXuNvYG/?a=5");
      entity.setContentType(KalicoContentType.FOOD_RECIPE.getValue());
      entity.setParaphrase(true);
      entity.setEmbedImages(false);
      entity.setUserId(userId);
      entity.setProjectName("Demo Project " + i);
    }
    projectRepo.saveAll(projectEntities);
    return projectEntities;
  }

  @SneakyThrows
  private void createMediaContent(List<Long> projectIds) {
    TypeReference<List<MediaContentEntity>> typeRef = new TypeReference<>() {};
    List<MediaContentEntity> contentEntities = objectMapper.readValue(
        loadFromFile("mr_public_video_content.json"), typeRef);
    for (Long projectId : projectIds) {
      for (MediaContentEntity contentEntity : contentEntities) {
        contentEntity.setId(null);
        contentEntity.setProjectId(projectId);
      }
      mediaContentRepo.saveAll(contentEntities);
    }
  }

  @SneakyThrows
  private void createSampledImages(List<Long> projectIds) {
    TypeReference<List<SampledImageEntity>> typeRef = new TypeReference<>() {};
    List<SampledImageEntity> sampledImageEntities = objectMapper.readValue(loadFromFile("mr_public_sampled_image.json"), typeRef);
    sampledImageEntities.sort(Comparator.comparing(SampledImageEntity::getImageKey));
    for (Long projectId : projectIds) {
      for (SampledImageEntity entity : sampledImageEntities) {
        entity.setId(null);
        entity.setProjectId(projectId);
    }
      sampledImageRepo.saveAll(sampledImageEntities);
    }
  }

  @SneakyThrows
  private String loadFromFile(String fileName) {
    InputStream resource = new ClassPathResource("seed/" + fileName).getInputStream();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource)) ) {
      String json = FileCopyUtils.copyToString(reader);
      return json;
    }
  }

  private String getPrimaryThumbnail(String key) {
    return awsProps.getCdn() + "/" + key;
  }
}
