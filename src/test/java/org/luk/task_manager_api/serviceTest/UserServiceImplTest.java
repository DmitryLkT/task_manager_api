package org.luk.task_manager_api.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.luk.task_manager_api.config.CustomUserDetails;
import org.luk.task_manager_api.config.jwt.JwtService;
import org.luk.task_manager_api.dto.JwtAuthentication;
import org.luk.task_manager_api.dto.LoginRequest;
import org.luk.task_manager_api.dto.RegisterRequest;
import org.luk.task_manager_api.dto.UserResponse;
import org.luk.task_manager_api.exception.customExceptions.InvalidCredentialsException;
import org.luk.task_manager_api.exception.customExceptions.UserAlreadyExistsException;
import org.luk.task_manager_api.exception.customExceptions.UserNotFoundException;
import org.luk.task_manager_api.model.Role;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.repository.RoleRepository;
import org.luk.task_manager_api.repository.UserRepository;
import org.luk.task_manager_api.service.impl.UserServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private RoleRepository roleRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private JwtService jwtService;
  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void register_successfulRegistration() {
    RegisterRequest request = new RegisterRequest(
      "Dmitry",
      "dmitry@gmail.com",
      "superPass147"
    );

    Role role = new Role("USER");

    User user = new User(
      "Dmitry",
      "dmitry@gmail.com",
      "passEncode",
      role
    );

    when(userRepository.existsByEmail("dmitry@gmail.com")).thenReturn(false);
    when(passwordEncoder.encode("superPass147")).thenReturn("passEncode");
    when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserResponse response = userService.register(request);

    assertEquals("Dmitry", response.getName());
    assertEquals("dmitry@gmail.com", response.getEmail());

    verify(userRepository).existsByEmail("dmitry@gmail.com");
    verify(passwordEncoder).encode("superPass147");
    verify(roleRepository).findByName("USER");
    verify(userRepository).save(any(User.class));
  }

  @Test
  void register_throwException() {
    RegisterRequest request = new RegisterRequest(
      "Dmitry",
      "dmitry@gmail.com",
      "superPass147"
    );

    when(userRepository.existsByEmail("dmitry@gmail.com")).thenReturn(true);

    assertThrows(UserAlreadyExistsException.class, () -> userService.register(request));

    verify(userRepository).existsByEmail("dmitry@gmail.com");
    verify(userRepository, never()).save(any());
  }

  @Test
  void register_createdDefaultRole() {
    RegisterRequest request = new RegisterRequest(
      "Dmitry",
      "dmitry@gmail.com",
      "superPass147"
    );

    Role role = new Role("USER");

    when(userRepository.existsByEmail("dmitry@gmail.com")).thenReturn(false);
    when(passwordEncoder.encode("superPass147")).thenReturn("passEncode");
    when(roleRepository.findByName("USER")).thenReturn(Optional.empty());
    when(roleRepository.save(any(Role.class))).thenReturn(role);

    userService.register(request);

    verify(roleRepository).findByName("USER");
    verify(roleRepository).save(any(Role.class));
  }

  @Test
  void login_successfulLogin() {
    LoginRequest request = new LoginRequest(
      "dmitry@gmail.com",
      "superPass147"
    );

    Role role = new Role("USER");


    User user = new User(
      "Dmitry",
      "dmitry@gmail.com",
      "passEncode",
      role
    );

    UserDetails userDetails = new CustomUserDetails(user);

    when(userRepository.findByEmail("dmitry@gmail.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
    when(jwtService.generateToken(userDetails)).thenReturn("accessToken");
    when(jwtService.generateRefreshToken(userDetails)).thenReturn("refreshToken");

    JwtAuthentication response = userService.login(request);

    assertEquals("accessToken", response.getToken());
    assertEquals("refreshToken", response.getRefreshToken());

    verify(userRepository).findByEmail("dmitry@gmail.com");
    verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
    verify(jwtService).generateToken(any(UserDetails.class));
    verify(jwtService).generateRefreshToken(any(UserDetails.class));
  }

  @Test
  void login_throwExceptionWhenPassword() {
    LoginRequest request = new LoginRequest(
      "dmitry@gmail.com",
      "superPass147"
    );

    User user = new User(
      "Dmitry",
      "dmitry@gmail.com",
      "passEncode",
      new Role()
    );

    when(userRepository.findByEmail("dmitry@gmail.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("superPass147", "passEncode")).thenReturn(false);

    assertThrows(InvalidCredentialsException.class, () -> userService.login(request));

    verify(passwordEncoder).matches("superPass147", "passEncode");
    verify(jwtService, never()).generateToken(any());
    verify(jwtService, never()).generateRefreshToken(any());
  }

  @Test
  void login_throwExceptionWhenUserNotFound() {
    LoginRequest request = new LoginRequest(
      "dmitry@gmail.com",
      "superPass147"
    );

    when(userRepository.findByEmail("dmitry@gmail.com")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.login(request));

    verify(userRepository).findByEmail("dmitry@gmail.com");
    verify(passwordEncoder, never()).matches(any(), any());
    verify(jwtService, never()).generateToken(any());
  }
}
