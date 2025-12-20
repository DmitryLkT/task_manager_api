package org.luk.task_manager_api.exception.customExceptions;

public class UserNotAuthenticatedException extends RuntimeException {
  public UserNotAuthenticatedException() {
    super("User not authenticated");
  }
}
