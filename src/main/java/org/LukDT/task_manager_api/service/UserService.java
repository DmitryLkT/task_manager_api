package org.LukDT.task_manager_api.service;

public interface UserService {
  UserResponse register(RegisterRequest request);

  JwtAuthentication login(LoginRequest request);

  UserResponse getCurrentUser();
}