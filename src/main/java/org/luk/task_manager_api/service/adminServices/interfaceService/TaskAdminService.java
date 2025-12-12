package org.luk.task_manager_api.service.adminServices.interfaceService;

import java.util.List;

import org.luk.task_manager_api.dto.TaskResponse;

public interface TaskAdminService {
  List<TaskResponse> getAllTasks();

  List<TaskResponse> getTasksByProject(Long projectId);

  List<TaskResponse> getTasksByUser(Long userId);
}
