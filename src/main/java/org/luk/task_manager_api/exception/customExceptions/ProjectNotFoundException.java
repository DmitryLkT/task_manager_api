package org.luk.task_manager_api.exception.customExceptions;

public class ProjectNotFoundException extends RuntimeException {
  public ProjectNotFoundException(String message) {
    super(String.format("Project not found %s", message));
  }
}
