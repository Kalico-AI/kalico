package ai.kalico.api.controller;

import ai.kalico.api.LeadApi;
import ai.kalico.api.service.lead.LeadService;
import com.kalico.model.ChannelPageableResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Biz Melesse
 * created on 02/05/23
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class LeadController implements LeadApi {
  private final LeadService leadService;

  @Override
  public ResponseEntity<ChannelPageableResponse> getChannelInfo(String query) {
    return ResponseEntity.ok(leadService.getChannelInfo(query));
  }

  @Override
  public ResponseEntity<byte[]> getUserEmailImage(String imageHash) {
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_PNG)
        .body(leadService.getUserEmailImage(imageHash));
  }
}
