package ai.kalico.api.service.blog;

import ai.kalico.api.data.postgres.repo.BlogPostRepo;
import ai.kalico.api.dto.VideoInfoDto;
import com.kalico.model.*;

import java.util.List;

/**
 * @author Biz Melesse
 * created on 10/17/22
 */
public interface BlogService {

  GenericResponse createBlogPost(VideoInfoDto dto);

  /**
   * Get a set number of the most recently published posts marked as
   * featured. These will be displayed on the homepage.
   *
   * @return
   */
  List<BlogPost> getFeaturedPosts();

  /**
   * Get the full blog post detail using its slug.
   *
   * @param slug a unique, SEO-optimized url slug of the post
   * @return
   */
  BlogPost getFullBlogPost(String slug);

  PageableResponse getAllPublishedPosts(Integer pageNumber, Integer limit);

  IngredientContent getBlogIngredients(String slug);

  BlogVideoContent getBlogVideoContent(String slug);

  /**
   * Entry point to start processing a video URL. Processing involves downloading the
   * video extracting texts, audio, and images from the video.
   *
   * @param request a supported video url
   * @return
   */
  GenericResponse processVideoRecipe(List<String> request);

  GenericResponse createBookmark(Slug slug);

  GenericResponse removeBookmark(Slug slug);

  List<BlogPost> getBookmarkedPosts(String userId);

  List<BlogPost> getUserSubmissions(String userId);

  GenericResponse incrementShareCount(Slug slug);

  GenericResponse likePost(Slug slug);
}
