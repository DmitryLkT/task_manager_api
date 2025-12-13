package org.luk.task_manager_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.luk.task_manager_api.dto.TaskRequest;
import org.luk.task_manager_api.dto.TaskResponse;
import org.luk.task_manager_api.model.Project;
import org.luk.task_manager_api.model.Status;
import org.luk.task_manager_api.model.Task;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.repository.ProjectRepository;
import org.luk.task_manager_api.repository.TaskRepository;
import org.luk.task_manager_api.service.CurrentUserService;
import org.luk.task_manager_api.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
  private final TaskRepository taskRepository;
  private final ProjectRepository projectRepository;
  private final CurrentUserService currentUserService;

  @Override
  @Transactional
  public List<TaskResponse> getAllTasksThisProject(String title) {
    Project project = getCurrentProject(title);

    return taskRepository.findByProjectId(project.getId()).stream()
            .map(t -> TaskResponse.fromEntity(t))
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public TaskResponse createdTask(String titleProject, TaskRequest request) {
    Project project = getCurrentProject(titleProject);

    Task task = new Task();
    if(request.getTitle() != null) {
      task.setTitle(request.getTitle());
    } else {
      task.setTitle("New Task");
    }
    
    if(request.getDescription() != null) {
      task.setDescription(request.getDescription());
    }
    task.setProject(project);
    task.setUser(currentUserService.getCurrentUser());

    taskRepository.save(task);
    
    return TaskResponse.fromEntity(task);
  }

  @Override
  @Transactional
  public TaskResponse updateTask(Long id, TaskRequest request) {
    Task task = getCurrentTask(id);

    if(request.getTitle() != null) {
      task.setTitle(request.getTitle());
    }
    if(request.getDescription() != null) {
      task.setDescription(request.getDescription());
    }

    taskRepository.save(task);

    return TaskResponse.fromEntity(task);
  }

  @Override
  @Transactional
  public void deleteTask(Long id) {
    Task task = getCurrentTask(id);

    taskRepository.delete(task);
  }

  @Override
  @Transactional
  public TaskResponse changeStatus(Long id, Status status) {
    Task task = getCurrentTask(id);
    task.setStatus(status);

    taskRepository.save(task);

    return TaskResponse.fromEntity(task);
  }
  
  private Project getCurrentProject(String title) {
    User user = currentUserService.getCurrentUser(); 
    Project project = projectRepository.findByTitle(title)
                  .orElseThrow(() -> new RuntimeException("Project not found"));
    currentUserService.checkProjectOwnership(project, user);
    
    return project;
  }

  private Task getCurrentTask(Long id) {
    User user = currentUserService.getCurrentUser();
    Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

    if(!task.getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Task not found");
    }

    return task;
  }
}
