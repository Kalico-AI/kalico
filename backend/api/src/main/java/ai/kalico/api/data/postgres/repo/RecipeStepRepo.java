package ai.kalico.api.data.postgres.repo;

import ai.kalico.api.data.postgres.entity.RecipeStepEntity;
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
public interface RecipeStepRepo extends JpaRepository<RecipeStepEntity, Long> {
  List<RecipeStepEntity> findByBlogPostId(Long docId);
  Integer deleteAllByBlogPostId(Long docId);
}
