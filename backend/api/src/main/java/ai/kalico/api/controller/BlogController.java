package ai.kalico.api.controller;

import ai.kalico.api.BlogApi;
import ai.kalico.api.service.blog.BlogService;
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
public class BlogController implements BlogApi {
  private final BlogService blogService;

  @Override
  public ResponseEntity<GenericResponse> createBookmark(Slug slug) {
    return ResponseEntity.ok(blogService.createBookmark(slug));
  }

  @Override
  public ResponseEntity<PageableResponse> getAllPublishedPosts(Integer pageNumber, Integer limit) {
    return ResponseEntity.ok(blogService.getAllPublishedPosts(pageNumber, limit));
  }

  @Override
  public ResponseEntity<IngredientContent> getBlogIngredients(String slug) {
    return ResponseEntity.ok(blogService.getBlogIngredients(slug));
  }

  @Override
  public ResponseEntity<BlogVideoContent> getBlogVideoContent(String slug) {
    return ResponseEntity.ok(blogService.getBlogVideoContent(slug));
  }

  @Override
  public ResponseEntity<List<BlogPost>> getBookmarkedPosts(String userId) {
    return ResponseEntity.ok(blogService.getBookmarkedPosts(userId));
  }

  @Override
  public ResponseEntity<List<BlogPost>> getFeaturedPosts() {
    return ResponseEntity.ok(blogService.getFeaturedPosts());
  }

  @Override
  public ResponseEntity<BlogPost> getFullBlogPost(String slug) {
    return ResponseEntity.ok(blogService.getFullBlogPost(slug));
  }

  @Override
  public ResponseEntity<List<BlogPost>> getUserSubmissions(String userId) {
    return ResponseEntity.ok(blogService.getUserSubmissions(userId));
  }

  @Override
  public ResponseEntity<GenericResponse> incrementShareCount(Slug slug) {
    return ResponseEntity.ok(blogService.incrementShareCount(slug));
  }

  @Override
  public ResponseEntity<GenericResponse> likePost(Slug slug) {
    return ResponseEntity.ok(blogService.likePost(slug));
  }

  @Override
  public ResponseEntity<GenericResponse> processVideoRecipe(List<String> request) {
    return ResponseEntity.ok(blogService.processVideoRecipe(request));
  }

  @Override
  public ResponseEntity<GenericResponse> removeBookmark(Slug slug) {
    return ResponseEntity.ok(blogService.removeBookmark(slug));
  }
}
