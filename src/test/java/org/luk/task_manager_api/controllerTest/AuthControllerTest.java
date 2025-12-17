package org.luk.task_manager_api.controllerTest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.luk.task_manager_api.controller.AuthController;
import org.luk.task_manager_api.dto.*;
import org.luk.task_manager_api.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
  @Mock
  private UserService userService; 

  @InjectMocks
  private AuthController authController;
  
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();

    mockMvc = MockMvcBuilders.standaloneSetup(authController).setValidator(validator).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void register_SuccessfulRegistration() throws Exception {
    RegisterRequest request = new RegisterRequest("Dmitry", "dmitry@gmail.com", "superPass147");
    UserResponse response = new UserResponse(1L, "Dmitry", "dmitry@gmail.com", "USER");

    when(userService.register(any(RegisterRequest.class))).thenReturn(response);

    String requestJson = objectMapper.writeValueAsString(request);

    mockMvc.perform(post("/auth/register")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Dmitry"))
        .andExpect(jsonPath("$.email").value("dmitry@gmail.com"))
        .andExpect(jsonPath("$.role").value("USER"));
    
    verify(userService, times(1)).register(request);
  }

  @Test
  void register_InvalidCredentials() throws Exception {
    RegisterRequest request = new RegisterRequest("Dmitry", "dmitry-gmail", "superPass147");

    String requestJson = objectMapper.writeValueAsString(request);

    mockMvc.perform(post("/auth/register")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void register_MissingRequiredFields() throws Exception {
    RegisterRequest request = new RegisterRequest("", "", "");

    String requestJson = objectMapper.writeValueAsString(request);

    mockMvc.perform(post("/auth/register")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(requestJson))
          .andExpect(status().isBadRequest());
  }

  @Test
  void login_SuccessfulLogin() throws Exception {
    LoginRequest request = new LoginRequest("dmitry@gmail.com", "superPass147");
    JwtAuthentication response = new JwtAuthentication("eyJhbGciOiJIUzUxMiJ9.eyJzdW...", "eyJhbGciOiJIUzUxMiJ9.eyJzdW...");

    when(userService.login(any(LoginRequest.class))).thenReturn(response);

    String requestStr = objectMapper.writeValueAsString(request);

    mockMvc.perform(post("/auth/login")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(requestStr))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.token").exists())
          .andExpect(jsonPath("$.refreshToken").exists())
          .andExpect(jsonPath("$.token").isString())
          .andExpect(jsonPath("$.refreshToken").isString());

    verify(userService, times(1)).login(any(LoginRequest.class));    
  }

  @Test
  void login_InvalidCredentials() throws Exception {
    LoginRequest request = new LoginRequest("dmitry@gmail.com", "superpass");

    String requestJson = objectMapper.writeValueAsString(request);

    mockMvc.perform(post("/auth/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void login_EmptyEmailOrPassword() throws Exception {
    LoginRequest requestEmptyPassword = new LoginRequest("dmitry@gmail.com", "");
    LoginRequest requestEmptyEmail = new LoginRequest("", "superPass147");

    String jsonEmptyPassword = objectMapper.writeValueAsString(requestEmptyPassword);

    mockMvc.perform(post("/auth/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content(jsonEmptyPassword))
        .andExpect(status().isBadRequest());

    String jsonEmptyEmail = objectMapper.writeValueAsString(requestEmptyEmail);

    mockMvc.perform(post("/auth/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content(jsonEmptyEmail))
        .andExpect(status().isBadRequest());
  }
}
