package ai.kalico.api.data.postgres.repo;

import ai.kalico.api.data.postgres.entity.VideoContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Bizuwork Melesse
 * created on October 20, 2022
 */
@Repository
@Transactional
public interface VideoContentRepo extends JpaRepository<VideoContentEntity, Long> {
  VideoContentEntity findByBlogPostId(Long docId);

  VideoContentEntity findByPermalink(String url);

  VideoContentEntity findByVideoId(String videoId);
}
