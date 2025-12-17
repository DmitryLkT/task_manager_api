package org.luk.task_manager_api.controller; 

import org.luk.task_manager_api.service.UserService;
import org.luk.task_manager_api.dto.RegisterRequest;
import org.luk.task_manager_api.dto.LoginRequest;
import org.luk.task_manager_api.dto.UserResponse;
import org.luk.task_manager_api.dto.JwtAuthentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
    UserResponse response = userService.register(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtAuthentication> login(@Valid @RequestBody LoginRequest request) {
    JwtAuthentication response = userService.login(request);
    return ResponseEntity.ok(response);
  }
}