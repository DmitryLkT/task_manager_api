package org.luk.task_manager_api.service.impl;

import org.luk.task_manager_api.config.CustomUserDetails;
import org.luk.task_manager_api.config.jwt.JwtService;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.model.Role;
import org.luk.task_manager_api.repository.RoleRepository;
import org.luk.task_manager_api.repository.UserRepository;
import org.luk.task_manager_api.service.UserService;
import org.luk.task_manager_api.dto.JwtAuthentication;
import org.luk.task_manager_api.dto.LoginRequest;
import org.luk.task_manager_api.dto.RegisterRequest;
import org.luk.task_manager_api.dto.UserResponse;
import org.luk.task_manager_api.exception.customExceptions.InvalidCredentialsException;
import org.luk.task_manager_api.exception.customExceptions.UserAlreadyExistsException;
import org.luk.task_manager_api.exception.customExceptions.UserNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  
  @Override
  @Transactional
  @PreAuthorize("hasRole('USER')")
  public UserResponse register(RegisterRequest request) {
    if(userRepository.existsByEmail(request.getEmail())) {
      throw new UserAlreadyExistsException(request.getEmail());
    }

    User user = new User();
    user.setName(request.getName().trim());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Role role = roleRepository.findByName("USER")
                .orElseGet(() -> createDefaultRole());

    user.setRole(role);
    
    userRepository.save(user);

    return UserResponse.fromEntity(user);
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('USER')")
  public JwtAuthentication login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
                 .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

    if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException();
    }

    UserDetails userDetails = new CustomUserDetails(user);

    String accessToken = jwtService.generateToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    return new JwtAuthentication(accessToken, refreshToken);
  }

  private Role createDefaultRole() {
    Role role = new Role();
    role.setName("USER");
    return roleRepository.save(role);
  }
}