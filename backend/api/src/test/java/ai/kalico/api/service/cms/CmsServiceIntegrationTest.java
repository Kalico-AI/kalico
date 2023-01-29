package com.kalico.api.service.cms;

import com.kalico.api.data.postgres.entity.*;
import com.kalico.api.data.postgres.repo.*;
import com.kalico.api.dto.VideoInfoDto;
import com.kalico.api.props.BlogPostProps;
import com.kalico.api.service.ServiceTestConfiguration;
import com.kalico.api.service.blog.BlogPostStatus;
import com.kalico.api.service.blog.BlogService;
import com.kalico.api.service.youtubej.model.videos.VideoDetails;
import com.kalico.api.service.youtubej.model.videos.VideoInfo;
import com.kalico.api.service.user.UserService;
import com.kalico.api.service.utils.Platform;
import com.kalico.api.utils.ServiceTestHelper;
import com.kalico.api.utils.migration.FlywayMigration;
import com.kalico.model.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Bizuwork Melesse
 * created on 8/22/21
 */
@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class CmsServiceIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private CmsService cmsService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private RecipeStepRepo recipeStepRepo;

    @Autowired
    private BlogPostRepo blogPostRepo;

    @Autowired
    private FlywayMigration flywayMigration;

    @Autowired
    private BlogPostProps blogPostProps;

    @Autowired
    private VideoContentRepo videoContentRepo;

    @Autowired
    private IngredientRepo ingredientRepo;

    @Autowired
    private SampledImageRepo sampledImageRepo;

    @Autowired
    private ServiceTestHelper testHelper;

    @Autowired
    private UserService userService;

    private final int numBlogPosts = 10;
    private final int numRecipeImages = 3;
    private final int numIngredients = 5;
    private final int sampledImageCount = 100;
    private List<BlogPostEntity> blogPostEntities;
    private List<Long> blogPostIds = new ArrayList<>();

    private final String userId = UUID.randomUUID().toString();

    @SneakyThrows
    @BeforeClass
    public void setup() {
      testHelper.prepareSecurity(userId);
    }

    @AfterTest
    public void teardown() {
    }

    @BeforeMethod
    public void beforeEachTest(Method method) {
        log.info("  Testcase: " + method.getName() + " has started");
        flywayMigration.migrate(true);

        UserProfileResponse response = userService.getOrCreateUserprofile();
        assertThat(response, is(notNullValue()));
        assertThat(response.getProfile().getFirebaseId(), startsWith(userId));

        blogPostEntities = createBlogPosts(numBlogPosts);
        blogPostIds = blogPostEntities.stream()
          .map(BlogPostEntity::getId)
          .collect(Collectors.toList());

        createRecipeImages(blogPostIds);
        createVideoContent(blogPostIds);
        createIngredients(blogPostIds);
        createSampledImages(blogPostIds);
    }


    @AfterMethod
    public void afterEachTest(Method method) {
        log.info("  Testcase: " + method.getName() + " has ended");
    }

  private void createBookmark(String postSlug) {
    Slug slug = new Slug().slug(postSlug);
    GenericResponse response = blogService.createBookmark(slug);
    assertThat(response, is(notNullValue()));
    assertThat(response.getStatus().get().toLowerCase(), startsWith("ok"));
  }

  private BookmarkRequest getBookmarkRequest(long docId) {
    return new BookmarkRequest()
        .docId(docId)
        .userId(userId);
  }

   @Test
    public void upsertBlogPostTest() {
      // create a new post
      BlogPost blogPost = new BlogPost();
      blogPost.setAuthor(JsonNullable.of("Test NG"));
      blogPost.setStatus(BlogPostStatus.DRAFT);
      blogPost.setSummary(JsonNullable.of("Test NG - summary"));
      blogPost.setTags(JsonNullable.of("fish;american;healthy diet"));
      blogPost.setTitle(JsonNullable.of("This is an excellent dish from Mexico"));
      blogPost.setIsFeatured(JsonNullable.of(false));
      blogPost.setPrimaryThumbnail(JsonNullable.of(UUID.randomUUID().toString()));
      BlogPostResponse createdPost = cmsService.upsertBlogPost(blogPost);
      assertBlogPost(createdPost.getBlogPost(), true);

      // Attempt to create one with a duplicate slug
      BlogPostResponse duplicatePost = cmsService.upsertBlogPost(blogPost);
      assertThat(duplicatePost, is(notNullValue()));
    }

   @Test
    public void createBookmarkTest() {
      createBookmark(blogPostEntities.get(0).getSlug());
      List<BlogPost> posts = blogService.getBookmarkedPosts(userId);
      assertThat(posts, is(notNullValue()));
      assertThat(posts.size(), is(equalTo(1)));
      assertBlogPost(posts.get(0), false);
    }

  private void createBatchBookmarks(long count) {
    for (int i = 0; i < count; i++) {
      createBookmark(blogPostEntities.get(i).getSlug());
    }
  }

 @Test
  public void deleteBookmarkTest() {
      createBookmark(blogPostEntities.get(0).getSlug());
      GenericResponse response = blogService.removeBookmark(new Slug().slug(blogPostEntities.get(0).getSlug()));
      assertThat(response, is(notNullValue()));
      assertThat(response.getStatus().get().toLowerCase(), startsWith("ok"));
      List<BlogPost> posts = blogService.getBookmarkedPosts(userId);
      assertThat(posts, is(notNullValue()));
      assertThat(posts.size(), is(equalTo(0)));
    }

   @Test
    public void getAllBookmarksTest() {
      long numBookmarks = 10;
      createBatchBookmarks(numBookmarks);
      List<BlogPost> posts = blogService.getBookmarkedPosts(userId);
      assertThat(posts, is(notNullValue()));
      assertThat(posts.size(), is(equalTo(1))); // only the first blog post is published
      for (BlogPost post : posts) {
        assertBlogPost(post, false);
      }
    }

   @Test
    public void createDuplicateBookmarksTest() {
      long numBookmarks = 10;
      createBatchBookmarks(numBookmarks);
      List<BlogPost> posts = blogService.getBookmarkedPosts(userId);
      assertThat(posts, is(notNullValue()));
      assertThat(posts.size(), is(equalTo(1))); // only the first blog post is published
      for (BlogPost post : posts) {
        assertBlogPost(post, false);
      }
    }

   @Test
    public void getAllSubmissionsTest() {
      VideoInfoDto dto = new VideoInfoDto();
      String url = "https://www.youtube.com/watch?v=U-0JCdjkREU";
      dto.setUrl(url);
      dto.setPlatform(Platform.YOUTUBE);
      dto.setFormat(null);
      VideoDetails details = getVideoDetails();
      VideoInfo info = new VideoInfo(details, null, null);
      dto.setVideoInfo(info);
      GenericResponse response = blogService.createBlogPost(dto);
      assertThat(response, is(notNullValue()));
      assertThat(response.getStatus().get().toLowerCase(), startsWith("ok"));
      List<BlogPost> posts = blogService.getUserSubmissions(userId);
      assertThat(posts, is(notNullValue()));
      assertThat(posts.size(), is(equalTo(1)));
      assertThat(posts.get(0).getSummary(), is(notNullValue()));
    }

  private VideoDetails getVideoDetails() {
      return new VideoDetails(UUID.randomUUID().toString());
  }

 @Test
    public void getAllFeaturedPostsTest() {
      List<BlogPost> posts = blogService.getFeaturedPosts();
      assertThat(posts.size(), is(equalTo(1)));
      assertBlogPost(posts.get(0), false);
    }

   @Test
    public void getAllPublishedPosts() {
      PageableResponse response = blogService.getAllPublishedPosts(0, 100);
      assertThat(response, is(notNullValue()));
      assertThat(response.getRecords().size(), is(equalTo(1)));
      assertBlogPost(response.getRecords().get(0), false);
    }

   @Test
    public void getVideoContentBySlugTest() {
      BlogVideoContent content = blogService.getBlogVideoContent(blogPostEntities.get(0).getSlug());
      assertThat(content, is(notNullValue()));
      assertThat(content.getTranscript(), is(notNullValue()));
    }

   @Test
    public void likeVideoTest() {
      String slug = blogPostEntities.get(0).getSlug();
      Slug request = new Slug().slug(slug);
      GenericResponse response = blogService.likePost(request);
      assertThat(response, is(notNullValue()));
      assertThat(response.getStatus().get().toLowerCase(), startsWith("ok"));

      // Fetch this from the db and assert
      BlogPost post = blogService.getFullBlogPost(slug);
      assertThat(post, is(notNullValue()));
      assertThat(post.getVideoMetadata().get().getLikeCount(), is(equalTo(1L)));
    }

   @Test
    public void updatePostBody() {
      Long docId = blogPostEntities.get(0).getId();
      PostBody body = new PostBody()
        .docId(docId)
        .body(UUID.randomUUID().toString());
      GenericResponse response = cmsService.updatePostBody(body);
      assertThat(response, is(notNullValue()));
      assertThat(response.getStatus().get().toLowerCase(), startsWith("ok"));

      BlogPost updatedPost = cmsService.getPostByDocId(blogPostEntities.get(0).getId());
      assertThat(updatedPost, is(notNullValue()));
    }

   @Test
    public void updateVideoContentTest() {
      Long docId = blogPostEntities.get(0).getId();
      CmsVideoContent content = new CmsVideoContent().docId(docId).rawTranscript(UUID.randomUUID().toString());
      GenericResponse response = cmsService.updateVideoContent(content);
      assertThat(response, is(notNullValue()));
      assertThat(response.getStatus().get().toLowerCase(), startsWith("ok"));
      CmsVideoContent updatedContent = cmsService.getCmsVideoContent(docId);
      assertVideoContent(updatedContent);
    }

   @Test
    public void upsertIngredientsTest() {
      Long docId = blogPostEntities.get(0).getId();
      IngredientContent ingredientContent = new IngredientContent().ingredients(new ArrayList<>());
      for (long i = 0; i < numIngredients; i++) {
        Ingredient ingredient = new Ingredient()
          .amount(2.0)
          .description(UUID.randomUUID().toString())
          .sortOrder(i)
          .units("spoons");
        ingredientContent.getIngredients().add(ingredient);
      }
     ingredientContent.setDocId(docId);
      GenericResponse response = cmsService.upsertIngredients(ingredientContent);
      assertThat(response, is(notNullValue()));
      assertThat(response.getStatus().get().toLowerCase(), startsWith("ok"));

      // Fetch the blog and assert
      assertIngredients(cmsService.getIngredients(docId), "spoons");
    }

   @Test
    public void upsertRecipeStepsTest() {
      Long docId = blogPostEntities.get(0).getId();
      RecipeContent recipeContent = new RecipeContent()
          .recipeSteps(new ArrayList<>())
          .primaryThumbnail(blogPostEntities.get(0).getPrimaryThumbnail())
          .docId(docId);
      int count = 10;
      for (long i = 0; i < count; i++) {
        RecipeStep step = new RecipeStep()
          .imageUrl(UUID.randomUUID().toString())
          .description(UUID.randomUUID().toString())
          .stepNumber(i);
        recipeContent.getRecipeSteps().add(step);
      }
      GenericResponse response = cmsService.upsertRecipeStep(recipeContent);
      assertThat(response, is(notNullValue()));
      assertThat(response.getStatus().get().toLowerCase(), startsWith("ok"));

      // Fetch the blog and assert
      RecipeContent recipeContentResponse2 = cmsService.getRecipeContent(docId);
      assertThat(recipeContentResponse2.getRecipeSteps().size(), is(equalTo(count)));
      assertRecipeContent(recipeContentResponse2, count);
    }

   @Test
    public void getAllBlogPostsTest() {
      List<BlogPost> response = cmsService.getAllPosts();
      assertThat(response, is(notNullValue()));
      assertThat(response.size(), is(equalTo(numBlogPosts)));
      for (BlogPost blogPost : response) {
        assertBlogPost(blogPost, false);
      }
    }

 @Test
  public void getBlogPostBySlug() {
    BlogPost response = cmsService.getPostBySlug(blogPostEntities.get(0).getSlug());
    assertThat(response, is(notNullValue()));
    assertBlogPost(response, true);
    assertRecipeContent(cmsService.getRecipeContent(response.getDocId()), numRecipeImages);
    assertVideoContent(cmsService.getCmsVideoContent(response.getDocId()));
    assertIngredients(cmsService.getIngredients(response.getDocId()), "cups");
    assertSampledImages(cmsService.getSampledImages(response.getDocId()));
  }

  private void assertSampledImages(List<String> sampledImages) {
      assertThat(sampledImages, is(notNullValue()));
      assertThat(sampledImages.size(), is(equalTo(1)));
    for (String sampledImage : sampledImages) {
      assertThat(sampledImage, containsString("https"));
    }
  }

  private void assertIngredients(IngredientContent ingredients, String units) {
      assertThat(ingredients, is(notNullValue()));
      assertThat(ingredients.getDocId(), is(notNullValue()));
      assertThat(ingredients.getIngredients().size(), is(equalTo(numIngredients)));
    for (Ingredient ingredient : ingredients.getIngredients()) {
      assertThat(ingredient.getAmount(), is(notNullValue()));
      assertThat(ingredient.getDescription(), is(notNullValue()));
      assertThat(ingredient.getUnits(), startsWith(units));
      assertThat(ingredient.getSortOrder(), is(greaterThanOrEqualTo(0L)));
    }
  }

  private void assertVideoContent(CmsVideoContent videoContent) {
      assertThat(videoContent, is(notNullValue()));
      assertThat(videoContent.getDescription(), is(notNullValue()));
      assertThat(videoContent.getRawTranscript(), is(notNullValue()));
      assertThat(videoContent.getRawTranscript(), is(notNullValue()));
      assertThat(videoContent.getOnScreenText(), is(notNullValue()));
//      assertThat(videoContent.getUrl(), is(notNullValue()));
//      assertThat(videoContent.getUrl(), containsString("https://youtube.com"));
      assertThat(videoContent.getDocId(), is(notNullValue()));
  }

  private void assertBlogPost(BlogPost response, boolean isFullBlogPost) {
      assertThat(response.getDocId(), is(notNullValue()));
      assertThat(response.getAuthor(), is(notNullValue()));
      assertThat(response.getTitle(), is(notNullValue()));
      assertThat(response.getTags(), is(notNullValue()));
      assertThat(response.getSummary(), is(notNullValue()));
      assertThat(response.getDateCreated(), is(notNullValue()));
      assertThat(response.getIsFeatured(), is(notNullValue()));
      assertThat(response.getSlug(), is(notNullValue()));
      assertThat(response.getStatus(), is(notNullValue()));
      if (response.getStatus().equals(BlogPostStatus.PUBLISHED)) {
        assertThat(response.getDatePublished(), is(notNullValue()));
      }
      assertThat(response.getPostUrl().get(), containsString(blogPostProps.getBaseSiteUrl()));
      assertThat(response.getPrimaryThumbnail(), is(notNullValue()));
  }

  private void assertRecipeContent(RecipeContent recipeContent, int imageCount) {
    assertThat(recipeContent, is(notNullValue()));
    assertThat(recipeContent.getDocId(), is(notNullValue()));
    assertThat(recipeContent.getPrimaryThumbnail(), is(notNullValue()));
    assertThat(recipeContent.getSamplingRate(), is(notNullValue()));
    assertThat(recipeContent.getRecipeSteps().size(), is(equalTo(imageCount)));
    for (RecipeStep recipeStep : recipeContent.getRecipeSteps()) {
      assertThat(recipeStep.getImageUrl(), is(notNullValue()));
      assertThat(recipeStep.getDescription(), is(notNullValue()));
      assertThat(recipeStep.getStepNumber(), is(notNullValue()));
      assertThat(recipeStep.getStepNumber(), is(greaterThanOrEqualTo(0L)));
    }
  }

  private List<BlogPostEntity> createBlogPosts(int count) {
      List<BlogPostEntity> entities = new ArrayList<>();
      for (int i = 0; i < count; i++) {
        BlogPostEntity entity = new BlogPostEntity();
        entity.setAuthor("Test NG");
        entity.setBody("Test NG - body");
        entity.setCuisine("American");
        entity.setSlug(UUID.randomUUID().toString());
        entity.setSummary("Test NG - summary");
        entity.setTags("fish;american;healthy diet");
        entity.setTitle(UUID.randomUUID().toString());
        entity.setIsFeatured(false);
        entity.setPublished(false);
        entity.setPrimaryThumbnail(UUID.randomUUID().toString());
        if (i == 0) {
          entity.setPublishedOn(LocalDateTime.now());
          entity.setIsFeatured(true);
          entity.setPublished(true);
        }
        entities.add(entity);
      }
      blogPostRepo.saveAll(entities);
      return entities;
    }

    private void createRecipeImages(List<Long> blogPostIds) {
      List<RecipeStepEntity> entities = new ArrayList<>();
      for (Long blogPostId : blogPostIds) {;
        for (long i = 0; i < numRecipeImages; i++) {
          RecipeStepEntity entity = new RecipeStepEntity();
          entity.setImageUrl(UUID.randomUUID().toString());
          entity.setBlogPostId(blogPostId);
          entity.setDescription(UUID.randomUUID().toString());
          entity.setStepNumber(i);
          entities.add(entity);

        }
      }
      recipeStepRepo.saveAll(entities);
    }

  private void createVideoContent(List<Long> blogPostIds) {
      List<VideoContentEntity> entities = new ArrayList<>();
      for (Long blogPostId : blogPostIds) {
        VideoContentEntity entity = new VideoContentEntity();
        entity.setVideoId(UUID.randomUUID().toString());
        entity.setTranscript(UUID.randomUUID().toString());
        entity.setRawTranscript(UUID.randomUUID().toString());
        entity.setDescription(UUID.randomUUID().toString());
        entity.setBlogPostId(blogPostId);
        entity.setOnScreenText(UUID.randomUUID().toString());
        entity.setUrl("https://youtube.com/" + UUID.randomUUID());
        entity.setTitle(UUID.randomUUID().toString());
        entity.setShareCount(0L);
        entity.setLikeCount(0L);
        entities.add(entity);
      }
      videoContentRepo.saveAll(entities);
  }

  private void createIngredients(List<Long> blogPostIds) {
      List<IngredientEntity> ingredientEntities = new ArrayList<>();
      for (Long blogPostId : blogPostIds) {
        for (long i = 0; i < numIngredients; i++) {
          IngredientEntity entity = new IngredientEntity();
          entity.setAmount("2");
          entity.setDescription(UUID.randomUUID().toString());
          entity.setBlogPostId(blogPostId);
          entity.setSortOrder(i);
          entity.setUnits("cups");
          ingredientEntities.add(entity);
        }
      }
      ingredientRepo.saveAll(ingredientEntities);
  }

  private void createSampledImages(List<Long> blogPostIds) {
      List<SampledImageEntity> sampledImageEntities = new ArrayList<>();
      for (Long blogPostId : blogPostIds) {
        SampledImageEntity entity = new SampledImageEntity();
        entity.setImageKey(UUID.randomUUID().toString());
        entity.setBlogPostId(blogPostId);
        sampledImageEntities.add(entity);
      }
      sampledImageRepo.saveAll(sampledImageEntities);
  }
}
