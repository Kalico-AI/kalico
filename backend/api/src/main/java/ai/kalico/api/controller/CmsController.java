package com.kalico.api.controller;

import com.kalico.api.CmsApi;
import com.kalico.api.service.cms.CmsService;
import com.kalico.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Biz Melesse
 * created on 10/17/22
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CmsController implements CmsApi {
  private final CmsService cmsService;

  @Override
  public ResponseEntity<GenericResponse> deleteGif(String url) {
    return ResponseEntity.ok(cmsService.deleteGif(url));
  }

  @Override
  public ResponseEntity<GifResponse> generateGif(GifRequest gifRequest) {
    return ResponseEntity.ok(cmsService.generateGif(gifRequest));
  }

  @Override
  public ResponseEntity<List<BlogPost>> getAllPosts() {
    return ResponseEntity.ok(cmsService.getAllPosts());
  }

  @Override
  public ResponseEntity<IngredientContent> getIngredients(Long docId) {
    return ResponseEntity.ok(cmsService.getIngredients(docId));
  }
  @Override
  public ResponseEntity<BlogPost> getPostById(Long docId) {
    return ResponseEntity.ok(cmsService.getPostByDocId(docId));
  }

  @Override
  public ResponseEntity<RecipeContent> getRecipeContent(Long docId) {
    return ResponseEntity.ok(cmsService.getRecipeContent(docId));
  }

  @Override
  public ResponseEntity<List<String>> getSampledImages(Long docId) {
    return ResponseEntity.ok(cmsService.getSampledImages(docId));
  }

  @Override
  public ResponseEntity<GenericResponse> publishBlogPost(Slug slug) {
    return ResponseEntity.ok(cmsService.publishBlogPost(slug));
  }

  @Override
  public ResponseEntity<GenericResponse> unPublishBlogPost(Slug slug) {
    return ResponseEntity.ok(cmsService.unPublishBlogPost(slug));
  }

  @Override
  public ResponseEntity<CmsVideoContent> getCmsVideoContent(Long docId) {
    return ResponseEntity.ok(cmsService.getCmsVideoContent(docId));
  }
  @Override
  public ResponseEntity<GenericResponse> upsertIngredients(IngredientContent ingredientContent) {
    return ResponseEntity.ok(cmsService.upsertIngredients(ingredientContent));
  }

  @Override
  public ResponseEntity<GenericResponse> upsertRecipeStep(RecipeContent recipeContent) {
    return ResponseEntity.ok(cmsService.upsertRecipeStep(recipeContent));
  }

  @Override
  public ResponseEntity<BlogPostResponse> upsertBlogPost(BlogPost blogPost) {
    return ResponseEntity.ok(cmsService.upsertBlogPost(blogPost));
  }
  @Override
  public ResponseEntity<GenericResponse> updateCmsVideoContent(CmsVideoContent videoContent) {
    return ResponseEntity.ok(cmsService.updateVideoContent(videoContent));
  }
}
