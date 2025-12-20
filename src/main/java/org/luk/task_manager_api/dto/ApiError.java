package org.luk.task_manager_api.dto;

import java.time.LocalDateTime;

public record ApiError(
  int status,
  String message,
  LocalDateTime timestamp
) {}
  
