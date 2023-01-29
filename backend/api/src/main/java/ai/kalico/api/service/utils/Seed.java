package com.kalico.api.service.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.api.data.postgres.entity.BlogPostEntity;
import com.kalico.api.data.postgres.entity.IngredientEntity;
import com.kalico.api.data.postgres.entity.RecipeStepEntity;
import com.kalico.api.data.postgres.entity.SampledImageEntity;
import com.kalico.api.data.postgres.entity.SubmissionEntity;
import com.kalico.api.data.postgres.entity.UserEntity;
import com.kalico.api.data.postgres.entity.VideoContentEntity;
import com.kalico.api.data.postgres.repo.BlogPostRepo;
import com.kalico.api.data.postgres.repo.IngredientRepo;
import com.kalico.api.data.postgres.repo.RecipeStepRepo;
import com.kalico.api.data.postgres.repo.SampledImageRepo;
import com.kalico.api.data.postgres.repo.SubmissionRepo;
import com.kalico.api.data.postgres.repo.UserRepo;
import com.kalico.api.data.postgres.repo.VideoContentRepo;
import com.kalico.api.props.AWSProps;
import com.kalico.api.props.BlogPostProps;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

/**
 * @author Biz Melesse created on 12/25/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Seed {
  private final BlogPostRepo blogPostRepo;
  private final RecipeStepRepo recipeStepRepo;
  private final VideoContentRepo videoContentRepo;
  private final IngredientRepo ingredientRepo;
  private final SampledImageRepo sampledImageRepo;
  private final SubmissionRepo submissionRepo;
  private final UserRepo userRepo;
  private final BlogPostProps blogPostProps;
  private final ObjectMapper mapper;
  private final AWSProps awsProps;
  private final Map<Long, String> primaryThumbnails = new HashMap<>();

  public void seed() {
    if (blogPostProps.getSeedDb() != null && blogPostProps.getSeedDb()) {
      int numRecords = 13;
      if (blogPostRepo.count() != numRecords) {
        try {
          List<BlogPostEntity> blogPostEntities = createBlogPosts(numRecords);
          createUserAccounts();
          List<Long> blogPostIds = blogPostEntities.stream()
              .map(BlogPostEntity::getId)
              .collect(Collectors.toList());
          createSampledImages();
          setPrimaryThumbnails();
          createVideoContent();
          createRecipeSteps(blogPostIds);
          createIngredients(blogPostIds);
          createSubmissions();
        } catch (Exception e) {
          log.error(e.getLocalizedMessage());
        }
      }
    }
  }

  @SneakyThrows
  private void createUserAccounts() {
    TypeReference<List<UserEntity>> typeRef = new TypeReference<>() {};
    List<UserEntity> userEntities = mapper.readValue(loadFromFile("mr_public_user.json"), typeRef);
    userRepo.saveAll(userEntities);
  }

  @SneakyThrows
  private void createSubmissions() {
    TypeReference<List<SubmissionEntity>> typeRef = new TypeReference<>() {};
    List<SubmissionEntity> submissionEntities = mapper.readValue(loadFromFile("mr_public_submission.json"), typeRef);
    submissionRepo.saveAll(submissionEntities);
  }

  @SneakyThrows
  private List<BlogPostEntity> createBlogPosts(int count) {
    TypeReference<List<BlogPostEntity>> typeRef = new TypeReference<>() {};
    List<BlogPostEntity> blogPostEntities = mapper.readValue(loadFromFile("mr_public_blog_post.json"), typeRef);
    for (BlogPostEntity entity : blogPostEntities) {
      entity.setAuthor("TheFoodieTakesFlight");
      entity.setBody("Foodwallah Post");
      entity.setCuisine("Global");
      entity.setSlug(UUID.randomUUID().toString());
      entity.setSummary("Foodwallah Post");
      entity.setTags("food");
      entity.setTitle("Demo Title: Easy and Delicious Korean Braised Tofu Recipe");
      entity.setIsFeatured(false);
      entity.setPublished(true);
      entity.setPrepTime("30 minutes");
      entity.setPublishedOn(LocalDateTime.now());
    }
    blogPostRepo.saveAll(blogPostEntities);
    return blogPostEntities;
  }

  private void createRecipeSteps(List<Long> blogPostIds) {
    List<RecipeStepEntity> entities = new ArrayList<>();
    for (Long blogPostId : blogPostIds) {;
      int numRecipeImages = 3;
      for (long i = 0; i < numRecipeImages; i++) {
        RecipeStepEntity entity = new RecipeStepEntity();
        entity.setStepNumber(i);
        entity.setImageUrl(getPrimaryThumbnail(primaryThumbnails.get(blogPostId)));
        entity.setBlogPostId(blogPostId);
        entity.setDescription("Allow the tofu and the sauce to simmer for a few minutes with a lid on. After taking the lid off, allow the sauce to reduce. Add scallions or green onions on top for garnishing. Serve and enjoy.");
        entities.add(entity);
      }
    }
    recipeStepRepo.saveAll(entities);
  }

  @SneakyThrows
  private void createVideoContent() {
    TypeReference<List<VideoContentEntity>> typeRef = new TypeReference<>() {};
    List<VideoContentEntity> videoContentEntities = mapper.readValue(loadFromFile("mr_public_video_content.json"), typeRef);
    videoContentRepo.saveAll(videoContentEntities);
  }

  private void createIngredients(List<Long> blogPostIds) {
    List<IngredientEntity> ingredientEntities = new ArrayList<>();
    for (Long blogPostId : blogPostIds) {
      int numIngredients = 5;
      for (long i = 0; i < numIngredients; i++) {
        IngredientEntity entity = new IngredientEntity();
        entity.setAmount("2");
        entity.setDescription("Chopped scallions (white parts)");
        entity.setBlogPostId(blogPostId);
        entity.setSortOrder(i);
        entity.setUnits("cups");
        ingredientEntities.add(entity);
      }
    }
    ingredientRepo.saveAll(ingredientEntities);
  }

  @SneakyThrows
  private void createSampledImages() {
    TypeReference<List<SampledImageEntity>> typeRef = new TypeReference<>() {};
    List<SampledImageEntity> sampledImageEntities = mapper.readValue(loadFromFile("mr_public_sampled_image.json"), typeRef);
    sampledImageEntities.sort(Comparator.comparing(SampledImageEntity::getImageKey));
    for (SampledImageEntity entity : sampledImageEntities) {
      if (!primaryThumbnails.containsKey(entity.getBlogPostId())) {
        primaryThumbnails.put(entity.getBlogPostId(), entity.getImageKey());
      }
    }
    sampledImageRepo.saveAll(sampledImageEntities);
  }

  @SneakyThrows
  private String loadFromFile(String fileName) {
    InputStream resource = new ClassPathResource("seed/" + fileName).getInputStream();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource)) ) {
      String json = FileCopyUtils.copyToString(reader);
      return json;
    }
  }

  private void setPrimaryThumbnails() {
    List<BlogPostEntity> blogPostEntities = blogPostRepo.findAll();
    for (BlogPostEntity entity : blogPostEntities) {
      String key = primaryThumbnails.get(entity.getId());
      if (key != null) {
        entity.setPrimaryThumbnail(getPrimaryThumbnail(key));
      }
    }
    if (blogPostEntities.size() > 0) {
      blogPostRepo.saveAll(blogPostEntities);
    }
  }

  private String getPrimaryThumbnail(String key) {
    return awsProps.getCdn() + "/" + key;
  }
}
