package ai.kalico.api.data.postgres.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Bizuwork Melesse
 * created on 4/21/22
 */
@Getter @Setter
@Entity
@Table(schema = "public", name = "authorized_user")
public class AuthorizedUserEntity {

    @Id
    @SequenceGenerator(name="pk_sequence_authorized_user",sequenceName="authorized_user_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.AUTO,generator="pk_sequence_authorized_user")
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "full_name")
    private String fullName;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "created_dt")
    private LocalDateTime createdDt = LocalDateTime.now();

    @Basic
    @Column(name = "updated_dt")
    private LocalDateTime updatedDt = LocalDateTime.now();
}
