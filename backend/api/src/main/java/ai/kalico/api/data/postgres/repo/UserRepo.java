package ai.kalico.api.data.postgres.repo;


import ai.kalico.api.data.postgres.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * @author Bizuwork Melesse
 * created on 4/21/22
 */
@Repository
@Transactional
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    UserEntity findByFirebaseId(String firebaseId);
}
