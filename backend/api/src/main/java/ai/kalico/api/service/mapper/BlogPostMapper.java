package ai.kalico.api.service.mapper;


import ai.kalico.api.data.postgres.entity.BlogPostEntity;
import ai.kalico.api.data.postgres.entity.IngredientEntity;
import ai.kalico.api.data.postgres.entity.RecipeStepEntity;
import ai.kalico.api.data.postgres.entity.VideoContentEntity;
import ai.kalico.api.service.blog.BlogPostStatus;
import com.kalico.model.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.util.ObjectUtils;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 25.02.2022
 */

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {JsonNullableMapper.class})
public interface BlogPostMapper {

  default List<BlogPost> map(List<BlogPostEntity> posts, String cdn, String videoFormat) {
    if ( posts == null ) {
      return new ArrayList<>();
    }

    List<BlogPost> list = new ArrayList<>();
    for ( BlogPostEntity blogPostEntity : posts ) {
      var post = map( blogPostEntity, cdn, videoFormat );
      if (post != null) {
        list.add( post );
      }
    }
    return list;
  }

  default BlogPost map(BlogPostEntity entity, String cdn, String videoFormat) {
    if ( entity == null ) {
      return null;
    }

    BlogPost blogPost = new BlogPost();
    blogPost.setDocId(entity.getId());
    blogPost.setSlug(JsonNullable.of(entity.getSlug()));
    blogPost.setTitle(JsonNullable.of(entity.getTitle()));
    blogPost.setTags(JsonNullable.of(entity.getTags()));
    blogPost.setSummary(JsonNullable.of(entity.getSummary()));
    blogPost.setAuthor(JsonNullable.of(entity.getAuthor()));
    blogPost.setDateCreated(entity.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
    if (entity.getPublishedOn() != null) {
      blogPost.setDatePublished(JsonNullable.of(entity.getPublishedOn().toEpochSecond(ZoneOffset.UTC)));
    }
    blogPost.setIsFeatured(JsonNullable.of(entity.getIsFeatured()));
    if (entity.getPublished() != null && entity.getPublished()) {
      blogPost.setStatus(BlogPostStatus.PUBLISHED);
    } else {
      blogPost.setStatus(BlogPostStatus.DRAFT);
    }
    blogPost.setPrimaryThumbnail(JsonNullable.of(entity.getPrimaryThumbnail()));
    if (entity.getVideoContent() != null) {
      String videoId = entity.getVideoContent().getVideoId();
      blogPost.setVideoMetadata(JsonNullable.of(new VideoMetadata()
          .caption(entity.getVideoContent().getDescription())
          .cdnUrl(getCdnUrl(cdn, videoId, videoFormat))
          .coverImage(entity.getPrimaryThumbnail())
          .creatorAvatar(entity.getVideoContent().getCreatorAvatar())
          .creatorHandle(entity.getVideoContent().getCreatorHandle())
          .likeCount(entity.getVideoContent().getLikeCount())
          .shareCount(entity.getVideoContent().getShareCount())
          .originalSource(entity.getVideoContent().getUrl())
      ));
    }
    return blogPost;
  }

  default String getCdnUrl(String cdn, String mediaId, String format) {
    return cdn + "/" + mediaId + "/" + mediaId + "." + format;
  }

  default CmsVideoContent mapVideoContent(VideoContentEntity entity) {
    if (entity == null) {
      return null;
    }
    CmsVideoContent content = new CmsVideoContent();
    content.setDocId(entity.getBlogPostId());
    content.setOnScreenText(entity.getOnScreenText());
    if (!ObjectUtils.isEmpty(entity.getTitle())) {
      content.setDescription(entity.getTitle() + "\n\n" + entity.getDescription());
    } else {
      content.setDescription(entity.getDescription());
    }
    content.setUrl(entity.getPermalink());
    content.setCaption(JsonNullable.of(entity.getCaption()));
    content.setRawTranscript(entity.getRawTranscript());
    return content;
  }

  default RecipeContent mapRecipeContent(List<RecipeStepEntity> recipeImageEntities,
      BlogPostEntity blogPostEntity, Double fps) {
    RecipeContent recipeContent = new RecipeContent();
    if (fps != null) {
      int secondsPerFrame = (int) (1.0/fps);
      recipeContent.setSamplingRate("1 frame every " + secondsPerFrame + " seconds.");
    }
    List<RecipeStep> recipeSteps = new ArrayList<>();
    if (!ObjectUtils.isEmpty(recipeImageEntities)) {
      if (blogPostEntity != null) {
        recipeContent.setPrimaryThumbnail(blogPostEntity.getPrimaryThumbnail());
        recipeContent.setDocId(blogPostEntity.getId());
      }
      for (RecipeStepEntity image : recipeImageEntities) {
        RecipeStep recipeStep = mapToRecipeStep(image);
        if (recipeStep != null) {
          recipeSteps.add(recipeStep);
        }
      }
      recipeSteps.sort(Comparator.comparing(RecipeStep::getStepNumber));
    }
    recipeContent.setRecipeSteps(recipeSteps);
    return recipeContent;
  }

  default RecipeStep mapToRecipeStep(RecipeStepEntity entity) {
    if (entity == null) {
      return null;
    }
    RecipeStep recipeImage = new RecipeStep();
    recipeImage.setImageUrl(entity.getImageUrl());
    recipeImage.setDescription(entity.getDescription());
    recipeImage.setStepNumber(entity.getStepNumber());
    return recipeImage;
  }

  List<Ingredient> mapIngredients(List<IngredientEntity> ingredientEntities);

  default Ingredient map(IngredientEntity entity) {
    if ( entity == null ) {
      return null;
    }

    Ingredient ingredient = new Ingredient();
    ingredient.setUnits(entity.getUnits());
    ingredient.setSortOrder(entity.getSortOrder());
    ingredient.setDescription(entity.getDescription());
    ingredient.setAmount(Double.parseDouble(entity.getAmount()));


    return ingredient;
  }
}
