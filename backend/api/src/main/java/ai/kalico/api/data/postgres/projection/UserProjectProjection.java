package ai.kalico.api.data.postgres.projection;

import java.time.LocalDateTime;

/**
 * @author Biz Melesse created on 2/16/23
 */
public interface UserProjectProjection {
  Long getProjectId();
  String getProjectName();
  String getContentUrl();
  String getEmail();
  String getFullName();
  LocalDateTime getProjectCreatedAt();
  LocalDateTime getRegisteredOn();
}
