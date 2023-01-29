package com.kalico.api.data.postgres.repo;


import com.kalico.api.data.postgres.entity.AuthorizedUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Bizuwork Melesse
 * created on 4/21/22
 */
@Repository
@Transactional
public interface AuthorizedUserRepo extends JpaRepository<AuthorizedUserEntity, Long> {
    AuthorizedUserEntity findByEmail(String email);
}

