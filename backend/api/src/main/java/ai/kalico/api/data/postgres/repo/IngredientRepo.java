package ai.kalico.api.data.postgres.repo;

import ai.kalico.api.data.postgres.entity.IngredientEntity;
import ai.kalico.api.data.postgres.entity.VideoContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Bizuwork Melesse
 * created on October 20, 2022
 */
@Repository
@Transactional
public interface IngredientRepo extends JpaRepository<IngredientEntity, Long> {
  List<IngredientEntity> findByBlogPostIdOrderBySortOrderAsc(Long docId);
  Integer deleteAllByBlogPostId(Long docId);
}
