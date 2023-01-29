package com.kalico.api.data.postgres.entity;

import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Bizuwork Melesse
 * created on December 03, 2022
 */
@Getter @Setter
@Entity
@Table(schema = "public", name = "bookmark")
public class BookmarkEntity {

    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="bookmark_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.AUTO,generator="pk_sequence_bookmark")
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "blog_post_id")
    private Long blogPostId;

    @Basic
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Basic
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
