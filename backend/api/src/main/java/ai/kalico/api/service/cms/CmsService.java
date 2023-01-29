package com.kalico.api.service.cms;

import com.kalico.model.*;

import java.util.List;

/**
 * @author Biz Melesse
 * created on 10/17/22
 */
public interface CmsService {

  GenericResponse deleteGif(String url);

  GifResponse generateGif(GifRequest gifRequest);

  /**
   * Get all posts sorted by time of creation in descending order.
   * @return
   */
  List<BlogPost> getAllPosts();

  List<BlogPost> getAllFeaturedPosts();

  PageableResponse getAllPublishedPosts(Integer pageNumber, Integer limit);

  /**
   * Get post detail by document ID.
   * @param slug
   * @return
   */
  BlogPost getPostBySlug(String slug);
  BlogPost getPostByDocId(Long docId);

  GenericResponse upsertIngredients(IngredientContent ingredientContent);

  GenericResponse updatePostBody(PostBody postBody);

  BlogPostResponse upsertBlogPost(BlogPost blogPost);

  GenericResponse updateVideoContent(CmsVideoContent videoContent);

  RecipeContent getRecipeContent(Long docId);
  CmsVideoContent getCmsVideoContent(Long docId);

  IngredientContent getIngredients(Long docId);

  List<String> getSampledImages(Long docId);

  GenericResponse upsertRecipeStep(RecipeContent recipeContent);

  List<BlogPost> getAllPublishedPostsByDocId(List<Long> docIds);

  List<BlogPost> getAllPostsByUserId(List<Long> docIds);

  GenericResponse publishBlogPost(Slug slug);

  GenericResponse unPublishBlogPost(Slug slug);
}
