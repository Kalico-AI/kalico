package ai.kalico.api.controller;

import ai.kalico.api.LeadApi;
import ai.kalico.api.ProjectApi;
import ai.kalico.api.service.lead.LeadService;
import ai.kalico.api.service.project.ProjectService;
import com.kalico.model.ChannelPageableResponse;
import com.kalico.model.ChannelRequest;
import com.kalico.model.ContentPreviewResponse;
import com.kalico.model.CreateProjectRequest;
import com.kalico.model.CreateProjectResponse;
import com.kalico.model.GenericResponse;
import com.kalico.model.MediaContent;
import com.kalico.model.PageableResponse;
import com.kalico.model.ProjectDetail;
import com.kalico.model.ProjectJobStatus;
import com.kalico.model.UpdateProjectContentRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public ResponseEntity<ChannelPageableResponse> getChannelInfo(ChannelRequest channelRequest) {
    return ResponseEntity.ok(leadService.getChannelInfo(channelRequest));
  }
}
