package org.luk.task_manager_api.service;

import java.util.List;

import org.luk.task_manager_api.dto.TaskRequest;
import org.luk.task_manager_api.dto.TaskResponse;
import org.luk.task_manager_api.model.Status;

public interface TaskService {
  List<TaskResponse> getAllTasksThisProject(String title);

  TaskResponse createdTask(String titleProject, TaskRequest request);

  TaskResponse updateTask(Long id, TaskRequest request);

  void deleteTask(Long id);

  TaskResponse changeStatus(Long id, Status status);
}
