package ai.kalico.api.data.postgres.repo;

import ai.kalico.api.data.postgres.entity.ProjectEntity;
import ai.kalico.api.data.postgres.projection.UserProjectProjection;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Query(value = "SELECT * " +
      "FROM project " +
      "WHERE user_id = ?1 AND processed = false "
      + "ORDER BY created_at DESC "
      + "LIMIT 1 ",
      nativeQuery = true)
  Optional<ProjectEntity> findPendingJob(String userId);

  @Query(value = "SELECT "
      + "p.id as projectId, "
      + "p.project_name as projectName, "
      + "p.created_at as projectCreatedAt, "
      + "mc.permalink as contentUrl, "
      + "u.email as email, "
      + "u.full_name as fullName, "
      + "u.created_at as registeredOn " +
      "FROM public.project p "
      + "JOIN public.user u ON u.firebase_id = p.user_id "
      + "INNER JOIN public.media_content mc ON mc.project_id = p.id ",
      nativeQuery = true)
  Page<UserProjectProjection> findAllUserProjects(Pageable pageable);
}
