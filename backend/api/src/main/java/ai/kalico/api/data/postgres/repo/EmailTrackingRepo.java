package ai.kalico.api.data.postgres.repo;


import ai.kalico.api.data.postgres.entity.EmailTrackingEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Bizuwork Melesse
 * created on February 18, 2023
 */
@Repository
@Transactional
public interface EmailTrackingRepo extends JpaRepository<EmailTrackingEntity, Long> {
    List<EmailTrackingEntity> findByEmailAndCampaignId(String email, String campaignId);
}
