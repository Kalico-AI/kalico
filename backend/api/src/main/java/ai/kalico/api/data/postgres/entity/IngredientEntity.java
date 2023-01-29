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
@Table(schema = "public", name = "ingredient")
public class IngredientEntity {

    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="ingredient_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.AUTO,generator="pk_sequence_ingredient")
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "blog_post_id")
    private Long blogPostId;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "amount")
    private String amount;

    @Basic
    @Column(name = "units")
    private String units;

    @Basic
    @Column(name = "sort_order")
    private Long sortOrder;

    @Basic
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Basic
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
