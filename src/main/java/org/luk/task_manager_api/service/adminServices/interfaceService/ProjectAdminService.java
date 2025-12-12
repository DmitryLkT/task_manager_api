package org.luk.task_manager_api.service.adminServices.interfaceService;

import java.util.List;

import org.luk.task_manager_api.dto.ProjectResponse;

public interface ProjectAdminService {
  List<ProjectResponse> getAllProjects(); 

  ProjectResponse getProjectById(Long projectId);

  List<ProjectResponse> getProjectsByUser(Long userId);
}
