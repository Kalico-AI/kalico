package ai.kalico.api.data.postgres.repo;

import ai.kalico.api.data.postgres.entity.ProjectEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Bizuwork Melesse
 * created on October 20, 2022
 */
@Repository
@Transactional
public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {
  @Query(value = "SELECT * " +
      "FROM project " +
      "WHERE user_id = ?1 AND processed = true "
      + "ORDER BY created_at DESC ",
      nativeQuery = true)
  List<ProjectEntity> findAllProjectsByUserId(String userId);

  @Query(value = "SELECT * " +
      "FROM project " +
      "WHERE user_id = ?1 AND id = ?2 AND processed = true ",
      nativeQuery = true)
  Optional<ProjectEntity> findProjectByUserIdAndProjectId(String userId, Long projectId);
}
