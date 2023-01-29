package ai.kalico.api.data.postgres.repo;

import ai.kalico.api.data.postgres.entity.ProjectEntity;
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
//  List<ProjectEntity> findAllByOrderByCreatedAtDesc();
//  ProjectEntity findBySlug(String slug);
//
//  @Query(value = "SELECT id " +
//    "FROM blog_post " +
//    "WHERE slug = ?1", nativeQuery = true)
//  Long findBlogPostIdBySlug(String slug);
//
//  @Query(value = "SELECT * " +
//    "FROM blog_post " +
//    "WHERE is_featured = true "
//      + "AND published = true " +
//    "ORDER BY created_at DESC", nativeQuery = true)
//  List<ProjectEntity> findAllFeaturedPosts();
//
//  @Query(value = "SELECT * " +
//    "FROM blog_post " +
//    "WHERE published = true ",
//      nativeQuery = true)
//  Page<ProjectEntity> findAllPublishedPosts(Pageable pageable);
//
//  @Query(value = "SELECT * " +
//      "FROM blog_post " +
//      "WHERE published = true "
//      + "AND id IN ?1 " +
//      "ORDER BY created_at DESC", nativeQuery = true)
//  List<ProjectEntity> findAllPublishedPostsByIds(List<Long> docIds);
//
//  @Query(value = "SELECT * " +
//      "FROM blog_post " +
//      "WHERE id IN ?1 " +
//      "ORDER BY created_at DESC", nativeQuery = true)
//  List<ProjectEntity> findAllPostsByDocId(List<Long> docIds);


  @Query(value = "SELECT * " +
      "FROM project " +
      "WHERE user_id = ?1 "
      + "ORDER BY created_dt DESC ",
      nativeQuery = true)
  List<ProjectEntity> findAllProjectsByUserId(String userId);

  @Modifying
  @Query(value = "SELECT * " +
      "FROM project " +
      "WHERE user_id = ?1 AND id = ?2",
      nativeQuery = true)
  Optional<ProjectEntity> findProjectByUserIdAndProjectId(String userId, Long projectId);
}
