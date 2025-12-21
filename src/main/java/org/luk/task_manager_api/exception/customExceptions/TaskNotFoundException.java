package org.luk.task_manager_api.exception.customExceptions;

public class TaskNotFoundException extends RuntimeException {
  public TaskNotFoundException(Long message) {
    super(String.format("Task not found: id %d", message));
  }
}
