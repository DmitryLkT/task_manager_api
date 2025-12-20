package org.luk.task_manager_api.exception.customExceptions;

public class ProjectAlreadyExistsException extends RuntimeException {
  public ProjectAlreadyExistsException() {
    super("Project with that title already exists");
  }
}
