package ai.kalico.api.service.utils;

import ai.kalico.api.data.postgres.entity.EmailTrackingEntity;
import ai.kalico.api.data.postgres.repo.EmailTrackingRepo;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 2/18/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeadServiceHelper {

  private final EmailTrackingRepo emailTrackingRepo;

  @Async
  public void logImageRequest(String imageHash, String ipAddress) {
    byte[] decodedBytes = Base64.getDecoder().decode(imageHash);
    String decodedHash = new String(decodedBytes);
    // An image hash is made up of user email address and campaign id
    log.info("Email Tracking: Image request received from {}", decodedHash);
    String[] tokens = decodedHash.split(":");
    if (tokens.length > 1) {
      String email = tokens[0];
      String campaignId = tokens[1];

      EmailTrackingEntity entity = null;
      // Locate the existing tracked emails  by campaign id and ip address. We need to do the lookup
      // using these two parameters so each device can only create a single record even if they're
      // opening multiple emails for the same campaign. This is the case when we are opening the
      // drafts and sending them.
      List<EmailTrackingEntity> trackingEntities = emailTrackingRepo.findByIpAddressAndCampaignId(email, campaignId);
      if (!trackingEntities.isEmpty()) {
        entity = trackingEntities.get(0);
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setNumOpened(entity.getNumOpened() + 1);
      } else {
        entity = new EmailTrackingEntity();
        entity.setCampaignId(campaignId);
        entity.setEmail(email);
        entity.setNumOpened(1L);
      }

      if (ipAddress != null) {
        entity.setIpAddress(ipAddress);
        emailTrackingRepo.save(entity);
      }
    } else {
      log.warn(this.getClass().getSimpleName()+ ".getUserEmailImage: Failed to parse image hash '{}'",
          decodedHash);
    }
  }

}
