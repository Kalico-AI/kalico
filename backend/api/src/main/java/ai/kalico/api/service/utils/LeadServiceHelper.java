package ai.kalico.api.service.utils;

import ai.kalico.api.data.postgres.entity.EmailTrackingEntity;
import ai.kalico.api.data.postgres.repo.EmailTrackingRepo;
import ai.kalico.api.props.IpAddressProps;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @author Biz Melesse created on 2/18/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeadServiceHelper {

  private final EmailTrackingRepo emailTrackingRepo;
  private final HttpServletRequest httpServletRequest;
  private final IpAddressProps ipAddressProps;

  @Async
  public void logImageRequest(String imageHash) {
    log.info("Email Tracking: Image request received from {}", imageHash);
    byte[] decodedBytes = Base64.getDecoder().decode(imageHash);
    String decodedHash = new String(decodedBytes);
    // An image hash is made up of user email address and campaign id
    String[] tokens = decodedHash.split(":");
    if (tokens.length > 1) {
      String email = tokens[0];
      String campaignId = tokens[1];

      EmailTrackingEntity entity = null;
      List<EmailTrackingEntity> trackingEntities = emailTrackingRepo.findByEmailAndCampaignId(email, campaignId);
      if (!trackingEntities.isEmpty()) {
        entity = trackingEntities.get(0);
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setNumOpens(entity.getNumOpens() + 1);
      } else {
        entity = new EmailTrackingEntity();
        entity.setCampaignId(campaignId);
        entity.setEmail(email);
        entity.setNumOpens(1L);
      }

      String ipAddress = getIp(getRequestHeaders());
      if (ipAddress != null) {
        entity.setIpAddress(ipAddress);
        emailTrackingRepo.save(entity);
      }
    } else {
      log.warn(this.getClass().getSimpleName()+ ".getUserEmailImage: Failed to parse image hash '{}'",
          decodedHash);
    }
  }

  private Map<String, String> getRequestHeaders() {
    // Normalize the headers by lower-casing all the keys
    Map<String, String> normalized = new HashMap<>();
    Enumeration<String> headers = httpServletRequest.getHeaderNames();
    while (headers.hasMoreElements()) {
      String header = headers.nextElement();
      normalized.put(header.toLowerCase(), httpServletRequest.getHeader(header));
    }
    return normalized;
  }


  @Nullable
  private String getIp(Map<String, String> normalizedHeaders) {
    Set<String> possibleIpAddressKeys = ipAddressProps.getHeaders();
    for (String header : possibleIpAddressKeys) {
      String ipList = normalizedHeaders.get(header.toLowerCase());
      if (!ObjectUtils.isEmpty(ipList)) {
        try {
          return ipList.split(",")[0];
        } catch (Exception ex) {
          log.error("RootConfiguration.getIp for ipList: {}, ex: {}"
              , ipList
              , ex.getLocalizedMessage());
        }
      }
    }
    // If the IP address is not set in any of the headers, then use the remote address
    String remoteAddress = httpServletRequest.getRemoteAddr();

    // Our own IP addresses are whitelisted so those must not be considered
    if (!whitelisted(remoteAddress)) {
      return remoteAddress;
    }
    return null;
  }

  private boolean whitelisted(String ipAddress) {
    // Check if an IP address is whitelisted. Because of subnetting, we only want
    // to check that the prefix
    for (String whitelistIpPrefix : ipAddressProps.getWhitelist()) {
      if (ipAddress.indexOf(whitelistIpPrefix) == 0) {
        return true;
      }
    }
    return false;
  }

}
