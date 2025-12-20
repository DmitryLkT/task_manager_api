package org.luk.task_manager_api.exception;

public class InvalidRefreshTokenException extends RuntimeException {
  public InvalidRefreshTokenException(String message) {
    super("Invalid or expired refresh token");
  }
}
