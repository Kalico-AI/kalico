package ai.kalico.api.service.blog;

import ai.kalico.api.data.postgres.entity.BlogPostEntity;
import ai.kalico.api.data.postgres.entity.BookmarkEntity;
import ai.kalico.api.data.postgres.entity.SubmissionEntity;
import ai.kalico.api.data.postgres.entity.VideoContentEntity;
import ai.kalico.api.data.postgres.repo.BlogPostRepo;
import ai.kalico.api.data.postgres.repo.BookmarkRepo;
import ai.kalico.api.data.postgres.repo.SubmissionRepo;
import ai.kalico.api.data.postgres.repo.VideoContentRepo;
import ai.kalico.api.dto.VideoInfoDto;
import ai.kalico.api.props.BlogPostProps;
import ai.kalico.api.service.cms.CmsService;
import ai.kalico.api.service.av.AVService;
import ai.kalico.api.utils.security.firebase.SecurityFilter;
import com.kalico.model.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse
 * created on 10/17/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
  private final CmsService cmsService;
  private final BlogPostRepo blogPostRepo;
  private final VideoContentRepo videoContentRepo;
  private final BlogPostProps blogPostProps;
  private final BookmarkRepo bookmarkRepo;
  private final SubmissionRepo submissionRepo;
  private final AVService AVService;
  private final SecurityFilter securityFilter;

  @Override
  public GenericResponse createBlogPost(VideoInfoDto dto) {
    if (dto != null) {
      BlogPostEntity blogPost = new BlogPostEntity();
      blogPost.setTitle("Untitled");
      blogPost.setSummary("");
      blogPost.setPublished(false);
      blogPost.setIsFeatured(false);
      blogPostRepo.save(blogPost);
      createSubmissionRecord(blogPost.getId());

      VideoContentEntity videoContent = new VideoContentEntity();
      videoContent.setLikeCount(0L);
      videoContent.setShareCount(0L);
      videoContent.setPermalink(dto.getPermalink());

      if (dto.getVideoInfo() != null && dto.getVideoInfo().details() != null) {
        videoContent.setVideoId(dto.getVideoInfo().details().videoId());
        if (dto.getVideoInfo().details().title() != null) {
          videoContent.setTitle(dto.getVideoInfo().details().title().replace("\n", "<br>"));
        }
        if (dto.getVideoInfo().details().description() != null) {
          videoContent.setDescription(dto.getVideoInfo().details().description().replace("\n", "<br>"));
        }
      } else {
        if (!ObjectUtils.isEmpty(dto.getVideoIdOverride())) {
          videoContent.setVideoId(dto.getVideoIdOverride());
          videoContent.setDescription(dto.getCaption().replace("\n", "<br>"));
        }
      }

      videoContent.setUrl(dto.getUrl());
      videoContent.setBlogPostId(blogPost.getId());
      videoContentRepo.save(videoContent);
      log.info("Saved initial video and blog post entry for url={}", dto.getUrl());
    } else {
      return new GenericResponse().error("VideoInfo not found");
    }
    return new GenericResponse().status("OK");
  }


  private void createSubmissionRecord(Long docId) {
    SubmissionEntity entity = new SubmissionEntity();
    entity.setBlogPostId(docId);
    entity.setUserId(securityFilter.getUser().getFirebaseId());
    submissionRepo.save(entity);
  }

  @Override
  public List<BlogPost> getFeaturedPosts() {
    return cmsService.getAllFeaturedPosts();
  }

  @Override
  public BlogPost getFullBlogPost(String slug) {
    return cmsService.getPostBySlug(slug);
  }

  @Override
  public PageableResponse getAllPublishedPosts(Integer pageNumber, Integer limit) {
    return cmsService.getAllPublishedPosts(pageNumber, limit);
  }

  @Override
  public IngredientContent getBlogIngredients(String slug) {
    return cmsService.getIngredients(blogPostRepo.findBlogPostIdBySlug(slug));
  }

  @Override
  public BlogVideoContent getBlogVideoContent(String slug) {
    CmsVideoContent content = cmsService.getCmsVideoContent(blogPostRepo.findBlogPostIdBySlug(slug));
    return new BlogVideoContent()
      .transcript(content.getRawTranscript())
      .url(content.getUrl());
  }

  @Override
  public GenericResponse processVideoRecipe(List<String> request) {
    List<String> unSupportedUrls = new ArrayList<>();
    if (!ObjectUtils.isEmpty(request)) {
      log.info("BlogServiceImpl.processVideoRecipe Received {} URL/s to process", request.size());
      List<String> uniqueUrls = new ArrayList<>((new HashSet<>(request)));
      for (String url : uniqueUrls) {
        String cleanUrl = cleanupUrl(url);
        if (isSupportedUrl(cleanUrl)) {
          // Check if this url has been processed before
          VideoContentEntity videoContentEntity = videoContentRepo.findByPermalink(cleanUrl);
          if (videoContentEntity == null) {
            AVService.startVideoProcessing(cleanUrl, this::createBlogPost);
          } else {
            log.info("BlogServiceImpl.processVideoRecipe Will not process url={}. Record already exists",
                cleanUrl);
          }
        } else {
          unSupportedUrls.add(url);
        }
      }

      int fail = unSupportedUrls.size();
      int success = request.size() - fail;
      return new GenericResponse().status(String.format("Urls submitted for processing. %s succeed and %s failed",
          success, fail));
    }
    return new GenericResponse().error("Invalid request. Missing URLs");
  }

  @Override
  public GenericResponse createBookmark(Slug slug) {
    BookmarkEntity entity = new BookmarkEntity();
    Long blogPostId = blogPostRepo.findBlogPostIdBySlug(slug.getSlug());
    if (blogPostId != null) {
      entity.setBlogPostId(blogPostId);
      entity.setUserId(securityFilter.getUser().getFirebaseId());
      bookmarkRepo.save(entity);
    }
    return new GenericResponse().status("OK");
  }

  @Override
  public GenericResponse removeBookmark(Slug slug) {
    Long blogPostId = blogPostRepo.findBlogPostIdBySlug(slug.getSlug());
    if (blogPostId != null) {
      BookmarkEntity entity = bookmarkRepo.findByUserIdAndBlogPostId(
          securityFilter.getUser().getFirebaseId(),
          blogPostId);
      if (entity != null) {
        bookmarkRepo.delete(entity);
      }
    }
    return new GenericResponse().status("OK");
  }

  @Override
  public List<BlogPost> getBookmarkedPosts(String userId) {
    List<BookmarkEntity> bookmarks = bookmarkRepo.findByUserIdOrderByCreatedAtDesc(userId);
    if (!ObjectUtils.isEmpty(bookmarks)) {
      return cmsService.getAllPublishedPostsByDocId(
          bookmarks
              .stream()
              .map(BookmarkEntity::getBlogPostId)
              .collect(Collectors.toList()));
    }
    return new ArrayList<>();
  }

  @Override
  public List<BlogPost> getUserSubmissions(String userId) {
    List<SubmissionEntity> submissions = submissionRepo.findByUserIdOrderByCreatedAtDesc(userId);
    if (!ObjectUtils.isEmpty(submissions)) {
      return cmsService.getAllPostsByUserId(
          submissions
              .stream()
              .map(SubmissionEntity::getBlogPostId)
              .collect(Collectors.toList()));
    }
    return new ArrayList<>();
  }

  @Override
  @Transactional
  public GenericResponse incrementShareCount(Slug slug) {
    VideoContentEntity videoEntity = getVideoEntity(slug.getSlug());
    if (videoEntity != null) {
      videoEntity.setShareCount(videoEntity.getShareCount() + 1);
      videoContentRepo.save(videoEntity);
    }
    return new GenericResponse().status("OK");
  }

  @Override
  public GenericResponse likePost(Slug slug) {
    VideoContentEntity videoEntity = getVideoEntity(slug.getSlug());
    if (videoEntity != null) {
      videoEntity.setLikeCount(videoEntity.getLikeCount() + 1);
      videoContentRepo.save(videoEntity);
    }
    return new GenericResponse().status("OK");
  }

  private boolean isSupportedUrl(String url) {
    if (isValidUrl(url)) {
      for (String supportedDomain : blogPostProps.getSupportedDomains()) {
        if (url.toLowerCase().contains(supportedDomain.toLowerCase())) {
          return true;
        }
      }
    }
   return false;
  }

  private String cleanupUrl(String url) {
    // TODO: Clean the URL to remove any tracking parameters
    return url;
  }

  private boolean isValidUrl(String url) {
    if (!ObjectUtils.isEmpty(url)) {
      try {
        URI uri = new URI(url);
        if (!ObjectUtils.isEmpty(uri.getHost())) {
          return true;
        }
      } catch (URISyntaxException e) {
        return false;
      }
    }
    return false;
  }

  private VideoContentEntity getVideoEntity(String slug) {
    Long id = blogPostRepo.findBlogPostIdBySlug(slug);
    if (id != null) {
      return videoContentRepo.findByBlogPostId(id);
    }
    return null;
  }
}
