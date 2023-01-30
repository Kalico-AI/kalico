package ai.kalico.api.service.project;

import ai.kalico.api.data.postgres.entity.MediaContentEntity;
import ai.kalico.api.data.postgres.entity.ProjectEntity;
import ai.kalico.api.data.postgres.entity.SampledImageEntity;
import ai.kalico.api.data.postgres.repo.MediaContentRepo;
import ai.kalico.api.data.postgres.repo.ProjectRepo;
import ai.kalico.api.data.postgres.repo.SampledImageRepo;
import ai.kalico.api.props.AWSProps;
import ai.kalico.api.props.ProjectProps;
import ai.kalico.api.service.av.AVService;
import ai.kalico.api.service.gif.GifService;
import ai.kalico.api.service.mapper.ProjectMapper;
import ai.kalico.api.utils.security.firebase.SecurityFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.CreateProjectRequest;
import com.kalico.model.CreateProjectResponse;
import com.kalico.model.GenericResponse;
import com.kalico.model.GifRequest;
import com.kalico.model.GifResponse;
import com.kalico.model.MediaContent;
import com.kalico.model.PageableResponse;
import com.kalico.model.ProjectDetail;
import com.kalico.model.UpdateProjectContentRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse created on 1/29/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
  private final GifService gifService;
  private final MediaContentRepo mediaContentRepo;
  private final SampledImageRepo sampledImageRepo;
  private final ProjectRepo projectRepo;
  private final AWSProps awsProps;
  private final SecurityFilter securityFilter;
  private final ProjectMapper projectMapper;
  private final ObjectMapper objectMapper;
  private final AVService avService;
  private final ProjectProps projectProps;

  @Override
  public CreateProjectResponse createProject(CreateProjectRequest createProjectRequest) {
    if (createProjectRequest != null) {
      // If a file is uploaded, use that. Otherwise, use the url
      String url = null;
      String file = null;
      String ext = null;
      if (createProjectRequest.getContentLink().isPresent()) {
        url = createProjectRequest.getContentLink().get();
      }
      if (createProjectRequest.getFile().isPresent()) {
        file = createProjectRequest.getFile().get();
      }
      if (createProjectRequest.getFileExtension().isPresent()) {
        ext = createProjectRequest.getFileExtension().get();
      }
     if (!ObjectUtils.isEmpty(file) &&  !ObjectUtils.isEmpty(ext)) {
        url = null;
      }  else if (!ObjectUtils.isEmpty(url)) {
       if (!isSupportedUrl(url)) {
         return new CreateProjectResponse().error("The link provided is not yet supported");
       }
       file = null;
       ext = null;
     } else {
        return new CreateProjectResponse().error("Please provide a link or upload a file");
      }
      String userId = securityFilter.getUser().getFirebaseId();
      ProjectEntity entity = new ProjectEntity();
      if (!ObjectUtils.isEmpty(createProjectRequest.getProjectName().get())) {
        entity.setProjectName(createProjectRequest.getProjectName().get());
      }
      // TODO: Users may try to create duplicate content from the same source link.
      //    We are allowing this for now.
      entity.setUserId(userId);
      entity.setEmbedImages(createProjectRequest.getEmbedImages().get());
      entity.setParaphrase(createProjectRequest.getParaphrase().get());
      entity.setContentType(createProjectRequest.getContentType().get().getValue());
      entity.setContentLink(url);
      projectRepo.save(entity);
      avService.processMedia(url, entity.getId(), file, ext);
      return new CreateProjectResponse()
          .status("OK")
          .projectId(entity.getId());
    }
    return new CreateProjectResponse().error("Encountered an error while creating the project");
  }
  @Override
  public GenericResponse deleteProject(Long id) {
    // TODO: remove the corresponding media files from S3
    if (id != null && id > 0) {
      projectRepo.deleteById(id);
      return new GenericResponse().status("OK");
    }
    return new GenericResponse().error("Invalid project id received. Deletion failed.");
  }

  @Override
  public PageableResponse getAllProjects() {
    String userId = securityFilter.getUser().getFirebaseId();
    List<ProjectEntity> projects = projectRepo.findAllProjectsByUserId(userId);
    PageableResponse response = new PageableResponse();
    response.setRecords(projectMapper.map(projects));
    response.setNumPages(1);
    response.setTotalRecords(response.getRecords().size());
     return response;
  }

  @Override
  public ProjectDetail getProjectById(Long id) {
    Optional<ProjectEntity> entityOpt = projectRepo.findById(id);
    return entityOpt.map(projectEntity -> projectMapper.map(projectEntity, objectMapper))
        .orElse(null);
  }

  @Override
  public GenericResponse updateProjectContent(
      UpdateProjectContentRequest updateProjectContentRequest) {
    if (updateProjectContentRequest != null &&
        updateProjectContentRequest.getId() != null) {
      String userId = securityFilter.getUser().getFirebaseId();
      Optional<ProjectEntity> project = projectRepo.findProjectByUserIdAndProjectId(userId,
          updateProjectContentRequest.getId());
      if (project.isPresent()) {
        ProjectEntity entity = project.get();
        entity.setUpdatedAt(LocalDateTime.now());
        if (!ObjectUtils.isEmpty(updateProjectContentRequest.getContent())) {
          try {
            entity.setContent(objectMapper.writeValueAsString(updateProjectContentRequest.getContent()));
          } catch (JsonProcessingException e) {
            log.error("ProjectServiceImpl.updateProjectContent: {}", e.getMessage());
          }
        } else {
          entity.setContent(null);
        }
        projectRepo.save(entity);
        return new GenericResponse().status("OK");
      }
    }
    return new GenericResponse().error("Project not found");
  }

  @Override
  public GenericResponse deleteGif(String url) {
    gifService.deleteGif(url);
    return new GenericResponse().status("OK");
  }

  @Override
  public GifResponse generateGif(GifRequest gifRequest) {
    String error = "";
    if (gifRequest.getProjectId() != null) {
      if (gifRequest.getStart() != null && gifRequest.getEnd() != null) {
        if (gifRequest.getStart() >= 0 && gifRequest.getEnd() >= 0) {
          if (gifRequest.getStart() < gifRequest.getEnd()) {
            MediaContentEntity entity = mediaContentRepo.findByProjectId(gifRequest.getProjectId());
            if (entity == null) {
              error = "Could not find a record of this video in the database";
            } else {
              //                  .url("https://media.giphy.com/media/sbERZAClpniGFsDKhS/giphy.gif")
              return new GifResponse()
                  .url(gifService.generateGif(entity.getMediaId(), gifRequest.getStart(), gifRequest.getEnd()))
                  .status("OK");
            }
          } else {
            error = "Start time cannot be equal to or greater than end time";
          }
        } else {
          error = "Start and end times must be equal to or greater than 0";
        }
      } else {
        error = "Start or end timestamps cannot be empty";
      }

    } else {
      error = "Document ID not provided";
    }
    return new GifResponse()
        .error(error);
  }

  @Override
  public List<String> getSampledImages(Long projectId) {
    List<SampledImageEntity> entities = sampledImageRepo.findByProjectIdOrderByImageKeyAsc(projectId);
    List<String> imageUrls = new ArrayList<>();
    if (!ObjectUtils.isEmpty(entities)) {
      return entities
          .stream()
          .map(it -> awsProps.getCdn() + "/" + it.getImageKey()).
          collect(Collectors.toList());
    }
    return imageUrls;
  }

  @Override
  public MediaContent getMediaContent(Long projectId) {
     return projectMapper.mapMediaContent(
        mediaContentRepo.findByProjectId(projectId));
  }

  private boolean isSupportedUrl(String url) {
    if (isValidUrl(url)) {
      for (String supportedDomain : projectProps.getSupportedDomains()) {
        if (url.toLowerCase().contains(supportedDomain.toLowerCase())) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isValidUrl(String url) {
    if (!ObjectUtils.isEmpty(url)) {
      try {
        URI uri = new URI(url);
        if (!ObjectUtils.isEmpty(uri.getHost())) {
          return true;
        }
      } catch (URISyntaxException e) {
        return false;
      }
    }
    return false;
  }
}
