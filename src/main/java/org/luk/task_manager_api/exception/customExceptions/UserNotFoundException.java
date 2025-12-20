package org.luk.task_manager_api.exception.customExceptions;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String email) {
    super(String.format("User not found: %s", email));
  }
}
