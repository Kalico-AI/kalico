package com.kalico.api.data.postgres.repo;

import com.kalico.api.data.postgres.entity.BlogPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface BlogPostRepo extends JpaRepository<BlogPostEntity, Long> {
  List<BlogPostEntity> findAllByOrderByCreatedAtDesc();
  BlogPostEntity findBySlug(String slug);

  @Query(value = "SELECT id " +
    "FROM blog_post " +
    "WHERE slug = ?1", nativeQuery = true)
  Long findBlogPostIdBySlug(String slug);

  @Query(value = "SELECT * " +
    "FROM blog_post " +
    "WHERE is_featured = true "
      + "AND published = true " +
    "ORDER BY created_at DESC", nativeQuery = true)
  List<BlogPostEntity> findAllFeaturedPosts();

  @Query(value = "SELECT * " +
    "FROM blog_post " +
    "WHERE published = true ",
      nativeQuery = true)
  Page<BlogPostEntity> findAllPublishedPosts(Pageable pageable);

  @Query(value = "SELECT * " +
      "FROM blog_post " +
      "WHERE published = true "
      + "AND id IN ?1 " +
      "ORDER BY created_at DESC", nativeQuery = true)
  List<BlogPostEntity> findAllPublishedPostsByIds(List<Long> docIds);

  @Query(value = "SELECT * " +
      "FROM blog_post " +
      "WHERE id IN ?1 " +
      "ORDER BY created_at DESC", nativeQuery = true)
  List<BlogPostEntity> findAllPostsByDocId(List<Long> docIds);
}
