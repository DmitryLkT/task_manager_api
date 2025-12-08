package org.luk.task_manager_api.service;

import java.util.List;

import org.luk.task_manager_api.dto.ProjectRequest;
import org.luk.task_manager_api.dto.ProjectResponse;
import org.luk.task_manager_api.model.Project;

public interface ProjectService {
  List<Project> getAllProjectsFromUser();

  ProjectResponse getProject(String title);

  ProjectResponse createdProject(ProjectRequest request);
  
  ProjectResponse updateProject(String title, ProjectRequest request);

  void deleteProject(String title);
}
