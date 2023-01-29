package com.kalico.api.data.postgres.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(schema = "public", name = "video_content")
public class VideoContentEntity {

    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="video_content_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.AUTO,generator="pk_sequence_video_content")
    @Column(name = "id")
    private Long id;

    @Basic
    @JsonProperty("blog_post_id")
    @Column(name = "blog_post_id")
    private Long blogPostId;

    @Basic
    @JsonProperty("video_id")
    @Column(name = "video_id")
    private String videoId;

    @Basic
    @Column(name = "url")
    private String url;

    @Basic
    @Column(name = "permalink")
    private String permalink;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "transcript")
    private String transcript;

    @Basic
    @Column(name = "caption")
    private String caption;

    @Basic
    @JsonProperty("raw_transcript")
    @Column(name = "raw_transcript")
    private String rawTranscript;

    @Basic
    @JsonProperty("on_screen_text")
    @Column(name = "on_screen_text")
    private String onScreenText;

    @Basic
    @JsonProperty("creator_avatar")
    @Column(name = "creator_avatar")
    private String creatorAvatar;

    @Basic
    @JsonProperty("creator_handle")
    @Column(name = "creator_handle")
    private String creatorHandle;

    @Basic
    @JsonProperty("share_count")
    @Column(name = "share_count")
    private Long shareCount;

    @Basic
    @JsonProperty("like_count")
    @Column(name = "like_count")
    private Long likeCount;

    @Basic
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Basic
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
