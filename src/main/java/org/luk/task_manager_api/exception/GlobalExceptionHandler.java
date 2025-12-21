package org.luk.task_manager_api.exception;

import java.time.LocalDateTime;

import org.luk.task_manager_api.dto.ApiError;
import org.luk.task_manager_api.exception.customExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidRefreshTokenException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiError handlerInvalidRefreshToken(InvalidRefreshTokenException e) {
    return new ApiError(
      401,
      e.getMessage(),
      LocalDateTime.now()
    );
  }

  @ExceptionHandler(UserNotAuthenticatedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiError handlerUserNotAuthenticated(UserNotAuthenticatedException e) {
    return new ApiError(
      401,
      e.getMessage(),
      LocalDateTime.now()
    );
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiError handlerUserNotFound(UserNotFoundException e) {
    return new ApiError(
      404,
      e.getMessage(),
      LocalDateTime.now()
    );
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiError handlerAccessDenied(AccessDeniedException e) {
    return new ApiError(
      403,
      e.getMessage(),
      LocalDateTime.now()
    );
  }

  @ExceptionHandler(ProjectAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ApiError handlerProjectAlreadyExists(ProjectAlreadyExistsException e) {
    return new ApiError(
      409,
      e.getMessage(),
      LocalDateTime.now()
    );
  }

  @ExceptionHandler(ProjectNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiError handlerProjectNotFound(ProjectNotFoundException e) {
    return new ApiError(
      404,
      e.getMessage(),
      LocalDateTime.now()
    );
  }
  
  @ExceptionHandler(UserAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ApiError handlerUserAlreadyExists(UserAlreadyExistsException e) {
    return new ApiError(
      409,
      e.getMessage(),
      LocalDateTime.now()
    );
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiError handlerInvalidCredentials(InvalidCredentialsException e) {
    return new ApiError(
      401,
      e.getMessage(),
      LocalDateTime.now()
    );
  }

  @ExceptionHandler(TaskNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiError handlerTaskNotFound(TaskNotFoundException e) {
    return new ApiError(
      404,
      e.getMessage(),
      LocalDateTime.now()
    );
  }
}
