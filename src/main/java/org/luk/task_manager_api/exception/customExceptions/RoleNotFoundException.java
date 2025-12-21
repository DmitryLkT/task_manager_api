package org.luk.task_manager_api.exception.customExceptions;

public class RoleNotFoundException extends RuntimeException {
  public RoleNotFoundException(String message) {
    super(String.format("A role with that name does not exist: %s", message));
  }
}
