package org.luk.task_manager_api.exception.customExceptions;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String message) {
    super(String.format("Email already exists: %s", message));
  }
}
