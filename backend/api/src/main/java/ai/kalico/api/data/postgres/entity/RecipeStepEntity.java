package ai.kalico.api.data.postgres.entity;

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
@Table(schema = "public", name = "recipe_step")
public class RecipeStepEntity {

    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="recipe_step_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.AUTO,generator="pk_sequence_recipe_step")
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "blog_post_id")
    private Long blogPostId;

    @Basic
    @Column(name = "image_url")
    private String imageUrl;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "step_number")
    private Long stepNumber;

    @Basic
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Basic
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
