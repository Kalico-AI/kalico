package ai.kalico.api.service.cms;

import ai.kalico.api.data.postgres.entity.*;
import ai.kalico.api.data.postgres.repo.*;
import ai.kalico.api.props.AWSProps;
import ai.kalico.api.props.BlogPostProps;
import ai.kalico.api.service.gif.GifService;
import ai.kalico.api.service.mapper.BlogPostMapper;
import com.kalico.model.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.util.ObjectUtils;


/**
 * @author Biz Melesse
 * created on 10/17/22
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CmsServiceImpl implements CmsService {
  private final GifService gifService;
  private final BlogPostRepo blogPostRepo;
  private final BlogPostMapper postMapper;
  private final BlogPostProps blogPostProps;
  private final RecipeStepRepo recipeStepRepo;
  private final VideoContentRepo videoContentRepo;
  private final IngredientRepo ingredientRepo;
  private final SampledImageRepo sampledImageRepo;
  private final AWSProps awsProps;
  private final Pattern NONLATIN = Pattern.compile("[^\\w-]");
  private final Pattern WHITESPACE = Pattern.compile("[\\s]");

  @Override
  public GenericResponse deleteGif(String url) {
    gifService.deleteGif(url);
    return new GenericResponse().status("OK");
  }

  @Override
  public GifResponse generateGif(GifRequest gifRequest) {
    String error = "";
    if (gifRequest.getDocId() != null) {
      if (gifRequest.getStart() != null && gifRequest.getEnd() != null) {
        if (gifRequest.getStart() >= 0 && gifRequest.getEnd() >= 0) {
          if (gifRequest.getStart() < gifRequest.getEnd()) {
            VideoContentEntity entity = videoContentRepo.findByBlogPostId(gifRequest.getDocId());
            if (entity == null) {
              error = "Could not find a record of this video in the database";
            } else {
              //                  .url("https://media.giphy.com/media/sbERZAClpniGFsDKhS/giphy.gif")
              return new GifResponse()
                  .url(gifService.generateGif(entity.getVideoId(), gifRequest.getStart(), gifRequest.getEnd()))
                  .status("OK");
            }
          } else {
            error = "Start time cannot be equal to or greater than end time";
          }
        } else {
          error = "Start and end times must be equal to or greater than 0";
        }
      } else {
        error = "Start or end timestamps cannot be empty";
      }

    } else {
      error = "Document ID not provided";
    }
    return new GifResponse()
        .error(error);
  }

  @Override
  public List<BlogPost> getAllPosts() {
    return litePosts(postMapper.map(
        blogPostRepo.findAllByOrderByCreatedAtDesc(),
        awsProps.getCdn() + "/" + awsProps.getStreamFolder(),
        awsProps.getVideoFormat()
    ));
  }

  @Override
  public List<BlogPost> getAllFeaturedPosts() {
    return litePosts(postMapper.map(
        blogPostRepo.findAllFeaturedPosts(),
        awsProps.getCdn() + "/" + awsProps.getStreamFolder(),
        awsProps.getVideoFormat()));
  }

  @Override
  public PageableResponse getAllPublishedPosts(Integer pageNumber, Integer limit) {

    Pageable pageable = PageRequest.of(pageNumber, limit,
        Sort.by("created_at").descending());
    Page<BlogPostEntity> pages =  blogPostRepo.findAllPublishedPosts(pageable);
    return new PageableResponse()
        .totalRecords(pages.getTotalElements())
        .numPages(pages.getTotalPages())
        .records(litePosts(postMapper.map(pages.getContent(),
            awsProps.getCdn() + "/" + awsProps.getStreamFolder(),
            awsProps.getVideoFormat())));
  }

  @Override
  public BlogPost getPostBySlug(String slug) {
    BlogPostEntity blogPostEntity = blogPostRepo.findBySlug(slug);
    return mapBlogPostEntity(blogPostEntity);
  }

  private BlogPost mapBlogPostEntity(BlogPostEntity blogPostEntity) {
    if (blogPostEntity != null) {
      BlogPost post = hydratedPost(postMapper.map(blogPostEntity,
          awsProps.getCdn() + "/" + awsProps.getStreamFolder(),
          awsProps.getVideoFormat()));
      List<RecipeStep> recipeSteps = new ArrayList<>();
      RecipeContent recipeContent = postMapper.mapRecipeContent(
          recipeStepRepo.findByBlogPostId(blogPostEntity.getId()),
          blogPostEntity,
          blogPostProps.getFps()
      );
      if (recipeContent != null) {
        recipeSteps = recipeContent.getRecipeSteps();
        recipeContent.setDocId(blogPostEntity.getId());
      }
      post.setRecipe(JsonNullable.of(new CompleteRecipe()
          .ingredients(
              postMapper.mapIngredients(
                  ingredientRepo.findByBlogPostIdOrderBySortOrderAsc(blogPostEntity.getId())))
          .steps(recipeSteps)
          .title(blogPostEntity.getTitle())
          .prepTime(blogPostEntity.getPrepTime())
          .cuisine(blogPostEntity.getCuisine())));
      if (blogPostEntity.getVideoContent() != null) {
        post.getRecipe().get()
            .originalSource(blogPostEntity.getVideoContent().getUrl());
      }
      return post;
    }
    return null;
  }

  private BlogPost hydratedPost(BlogPost post) {
    if (post != null) {
      post.setPostUrl(JsonNullable.of(blogPostProps.getBaseSiteUrl() + "/watch/" + post.getSlug().get()));
    }
    return post;
  }

  @Override
  public BlogPost getPostByDocId(Long docId) {
    Optional<BlogPostEntity> entityOpt = blogPostRepo.findById(docId);
    entityOpt.ifPresent(blogPostEntity -> blogPostEntity.setVideoContent(
        videoContentRepo.findByBlogPostId(blogPostEntity.getId())));
    return mapBlogPostEntity(entityOpt.get());
  }

  @Override
  @Transactional
  public GenericResponse upsertIngredients(IngredientContent ingredientContent) {
    if (ingredientContent != null && ingredientContent.getDocId() != null) {
      ingredientRepo.deleteAllByBlogPostId(ingredientContent.getDocId());
      List<IngredientEntity> ingredientEntities = new ArrayList<>();
      for (Ingredient ingredient : ingredientContent.getIngredients()) {
        IngredientEntity entity = new IngredientEntity();
        entity.setUnits(ingredient.getUnits());
        entity.setDescription(ingredient.getDescription());
        entity.setSortOrder(ingredient.getSortOrder());
        entity.setAmount(ingredient.getAmount().toString());
        entity.setBlogPostId(ingredientContent.getDocId());
        ingredientEntities.add(entity);
      }
      ingredientRepo.saveAll(ingredientEntities);
    }
    return new GenericResponse().status("OK");
  }

  @SneakyThrows
  @Override
  public GenericResponse updatePostBody(PostBody postBody) {
    if (postBody != null && postBody.getDocId() != null) {
      Optional<BlogPostEntity> entityOpt = blogPostRepo.findById(postBody.getDocId());
      if (entityOpt.isPresent()) {
        entityOpt.get().setBody(postBody.getBody());
        entityOpt.get().setUpdatedAt(LocalDateTime.now());
        blogPostRepo.save(entityOpt.get());
      }
    }
    return new GenericResponse().status("OK");
  }

  @Override
  public BlogPostResponse upsertBlogPost(BlogPost blogPost) {
    if (blogPost != null) {
      BlogPostEntity entity = new BlogPostEntity();
      if (blogPost.getDocId() != null) {
        // Update an existing doc
        Optional<BlogPostEntity> entityOpt = blogPostRepo.findById(blogPost.getDocId());
        if (entityOpt.isPresent()) {
          entity = entityOpt.get();
        }
      }
      String slug = toSlug(entity.getSlug(), blogPost.getTitle().get(), true);
      if (slug != null) {
        entity.setSlug(slug);
        if (blogPost.getRecipe().isPresent()) {
          entity.setCuisine(blogPost.getRecipe().get().getCuisine().get());
          entity.setPrepTime(blogPost.getRecipe().get().getPrepTime().get());
        }
        entity.setTitle(blogPost.getTitle().get());
        entity.setSummary(blogPost.getSummary().get());
        entity.setPrimaryThumbnail(blogPost.getPrimaryThumbnail().get());
        entity.setTags(blogPost.getTags().get());
        entity.setIsFeatured(blogPost.getIsFeatured().get());
        entity.setPublished(false);
        entity.setAuthor(blogPost.getAuthor().get());
        entity.setUpdatedAt(LocalDateTime.now());
        if (blogPost.getStatus().equalsIgnoreCase("published")) {
          entity.setPublished(true);
          if (entity.getPublishedOn() == null) {
            entity.setPublishedOn(LocalDateTime.now());
          }
        }
        blogPostRepo.save(entity);
        return new BlogPostResponse()
            .status("OK")
            .blogPost(getPostBySlug(entity.getSlug()));
      } else {
        return new BlogPostResponse()
            .error("Cannot save the blog until an SEO-friendly title is set");
      }
    }
    return new BlogPostResponse()
        .error("No data received for updating");
  }

  @Override
  public GenericResponse updateVideoContent(CmsVideoContent videoContent) {
    if (videoContent != null && videoContent.getDocId() != null) {
      VideoContentEntity entity = videoContentRepo.findByBlogPostId(videoContent.getDocId());
      if (entity != null) {
        entity.setUpdatedAt(LocalDateTime.now());
        // raw transcript cannot be edited
        if (videoContent.getCaption() != null && videoContent.getCaption().isPresent()) {
          entity.setCaption(videoContent.getCaption().get());
        }
        videoContentRepo.save(entity);
      }
    }
    return new GenericResponse().status("OK");
  }

  @Override
  public RecipeContent getRecipeContent(Long docId) {
    Optional<BlogPostEntity> blogPostEntity = blogPostRepo.findById(docId);
    if (blogPostEntity.isPresent()) {
      return postMapper.mapRecipeContent(
          recipeStepRepo.findByBlogPostId(blogPostEntity.get().getId()),
          blogPostEntity.get(),
          blogPostProps.getFps());
    }
    return new RecipeContent();
  }

  private String toSlug(String oldSlug, String title, boolean forceUpdate) {
    if (title.equalsIgnoreCase("untitled")) {
      return null;
    }
    else if (oldSlug != null && !forceUpdate) {
      return oldSlug;
    }
    title = title.replace(":", " ");
    String nowhitespace = WHITESPACE.matcher(title).replaceAll("-");
    String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
    String slug = NONLATIN.matcher(normalized).replaceAll("");
    slug = slug.toLowerCase(Locale.ENGLISH);

    // Check that the new slug does not exist
    if (blogPostRepo.findBySlug(slug) == null) {
      return slug;
    }
    return "";
  }

  @Override
  @Transactional
  public GenericResponse upsertRecipeStep(RecipeContent recipeContent) {
    if (recipeContent != null && recipeContent.getDocId() != null) {
      Optional<BlogPostEntity> entityOpt = blogPostRepo.findById(recipeContent.getDocId());
      if (entityOpt.isPresent()) {
        BlogPostEntity blogPostEntity = entityOpt.get();
        Long blogPostId = blogPostEntity.getId();
        if (blogPostId != null) {
          recipeStepRepo.deleteAllByBlogPostId(blogPostId);
          List<RecipeStepEntity> recipeStepEntities = new ArrayList<>();
          for (RecipeStep step : recipeContent.getRecipeSteps()) {
            RecipeStepEntity entity = new RecipeStepEntity();
            entity.setStepNumber(step.getStepNumber());
            entity.setImageUrl(parseImageUrls(step.getImageUrl()));
            entity.setDescription(step.getDescription());
            entity.setBlogPostId(blogPostId);
            recipeStepEntities.add(entity);
          }
          recipeStepRepo.saveAll(recipeStepEntities);
          blogPostEntity.setPrimaryThumbnail(recipeContent.getPrimaryThumbnail());
          blogPostEntity.setUpdatedAt(LocalDateTime.now());
          blogPostRepo.save(blogPostEntity);
        }
      }
    }
    return new GenericResponse().status("OK");
  }

  private String parseImageUrls(String imageUrls) {
    String regex = "(http(?:s)?://.+(?:.jpg|.gif))";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(imageUrls);
    List<String> matches = new ArrayList<>();
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    if (!matches.isEmpty()) {
      return String.join("\n", matches);
    }
    return imageUrls;
  }

  @Override
  public List<BlogPost> getAllPublishedPostsByDocId(List<Long> docIds) {
    return litePosts(postMapper.map(blogPostRepo.findAllPublishedPostsByIds(docIds),
        awsProps.getCdn() + "/" + awsProps.getStreamFolder(),
        awsProps.getVideoFormat()));
  }

  @Override
  public List<BlogPost> getAllPostsByUserId(List<Long> docIds) {
    return litePosts(postMapper.map(blogPostRepo.findAllPostsByDocId(docIds),
        awsProps.getCdn() + "/" + awsProps.getStreamFolder(),
        awsProps.getVideoFormat()));
  }

  @Override
  public GenericResponse publishBlogPost(Slug slug) {
    return updatePublicationStatus(slug, true);
  }

  @Override
  public GenericResponse unPublishBlogPost(Slug slug) {
    return updatePublicationStatus(slug, false);
  }

  @Override
  public CmsVideoContent getCmsVideoContent(Long docId) {
    CmsVideoContent videoContent = postMapper.mapVideoContent(videoContentRepo.findByBlogPostId(docId));
    if (videoContent != null) {
      videoContent.setDocId(docId);
    }
    return videoContent;
  }

  @Override
  public IngredientContent getIngredients(Long docId) {
    List<Ingredient> ingredients = postMapper.mapIngredients(ingredientRepo.findByBlogPostIdOrderBySortOrderAsc(docId));
    return new IngredientContent()
      .ingredients(ingredients)
      .docId(docId);
  }

  @Override
  public List<String> getSampledImages(Long docId) {
    List<SampledImageEntity> entities = sampledImageRepo.findByBlogPostIdOrderByImageKeyAsc(docId);
    List<String> imageUrls = new ArrayList<>();
    if (!ObjectUtils.isEmpty(entities)) {
      return entities
          .stream()
          .map(it -> awsProps.getCdn() + "/" + it.getImageKey()).
          collect(Collectors.toList());
    }
    return imageUrls;
  }

  private List<BlogPost> litePosts(List<BlogPost> allPosts) {
    for (BlogPost blogPost : allPosts) {
      blogPost.setPostUrl(JsonNullable.of(blogPostProps.getBaseSiteUrl() + "/" + blogPost.getSlug()));
    }
    return allPosts;
  }

  private GenericResponse updatePublicationStatus(Slug slug, boolean publish) {
    String msg = "Failed to publish. Please ensure the blog title is present and SEO-friendly";
    if (!ObjectUtils.isEmpty(slug.getSlug())) {
      if (slug.getSlug() == null || slug.getSlug().equalsIgnoreCase("untitled") && publish) {
        return new GenericResponse()
            .error(msg);
      }
      BlogPostEntity blogPostEntity = blogPostRepo.findBySlug(slug.getSlug());
      if (blogPostEntity != null && slug.getSlug() != null) {
        blogPostEntity.setPublished(publish);
        if (publish) {
          blogPostEntity.setPublishedOn(LocalDateTime.now());
          // Update the slug
          String nSlug = toSlug(slug.getSlug(), blogPostEntity.getTitle(), true);
          if (!ObjectUtils.isEmpty(nSlug)) {
            blogPostEntity.setSlug(nSlug);
          }
        } else {
          blogPostEntity.setPublishedOn(null);
        }
        blogPostRepo.save(blogPostEntity);
      }
      return new GenericResponse().status("OK");
    }
    return new GenericResponse().error(msg);
  }
}
