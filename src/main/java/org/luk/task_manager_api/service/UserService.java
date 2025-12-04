package org.luk.task_manager_api.service;

import org.luk.task_manager_api.dto.JwtAuthentication;
import org.luk.task_manager_api.dto.LoginRequest;
import org.luk.task_manager_api.dto.RegisterRequest;
import org.luk.task_manager_api.dto.UserResponse;

public interface UserService {
  UserResponse register(RegisterRequest request);

  JwtAuthentication login(LoginRequest request);

  UserResponse getCurrentUser();
}