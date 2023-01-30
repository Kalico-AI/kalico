package ai.kalico.api.service.project;

import com.kalico.model.CreateProjectRequest;
import com.kalico.model.CreateProjectResponse;
import com.kalico.model.GenericResponse;
import com.kalico.model.GifRequest;
import com.kalico.model.GifResponse;
import com.kalico.model.MediaContent;
import com.kalico.model.PageableResponse;
import com.kalico.model.ProjectDetail;
import com.kalico.model.ProjectJobStatus;
import com.kalico.model.UpdateProjectContentRequest;
import java.util.List;

/**
 * @author Biz Melesse created on 1/29/23
 */
public interface ProjectService {

  /**
   * Create a new project
   *
   * @param createProjectRequest project metadata and an optional uploaded media content
   * @return
   */
  CreateProjectResponse createProject(CreateProjectRequest createProjectRequest);
  GenericResponse deleteProject(Long id);
  PageableResponse getAllProjects();
  ProjectDetail getProjectById(Long id);

  GenericResponse updateProjectContent(
      UpdateProjectContentRequest updateProjectContentRequest);

  GenericResponse deleteGif(String url);

  GifResponse generateGif(GifRequest gifRequest);

  List<String> getSampledImages(Long projectId);

  MediaContent getMediaContent(Long projectId);

  ProjectJobStatus getProjectJobStatus(Long projectId);

}
