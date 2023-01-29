package ai.kalico.api.data.postgres.repo;

import ai.kalico.api.data.postgres.entity.BookmarkEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Bizuwork Melesse
 * created on December 03, 2022
 */
@Repository
@Transactional
public interface BookmarkRepo extends JpaRepository<BookmarkEntity, Long> {
  List<BookmarkEntity> findByUserIdOrderByCreatedAtDesc(String userId);

  BookmarkEntity findByUserIdAndBlogPostId(String userId, Long docId);
}
