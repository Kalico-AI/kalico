package com.kalico.api.data.postgres.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Bizuwork Melesse
 * created on October 20, 2022
 */
@Getter @Setter
@Entity
@Table(schema = "public", name = "blog_post")
public class BlogPostEntity {

    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="blog_post_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.AUTO,generator="pk_sequence_blog_post")
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "slug")
    private String slug;

    @Basic
    @Column(name = "summary")
    private String summary;

    @Basic
    @Column(name = "prep_time")
    private String prepTime;

    @Basic
    @Column(name = "author")
    private String author;

    @Basic
    @Column(name = "cuisine")
    private String cuisine;

    @Basic
    @Column(name = "body")
    private String body;

    @Basic
    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Basic
    @Column(name = "published")
    private Boolean published;

    @Basic
    @Column(name = "tags")
    private String tags;

    @Basic
    @Column(name = "primary_thumbnail")
    private String primaryThumbnail;

    @Basic
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Basic
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Basic
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "blog_post_id", insertable = false, updatable = false)
    private VideoContentEntity videoContent;
}
