import org.LukDT.task_manager_api.service.UserService;
import org.LukDT.task_manager_api.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
    UserResponse response = userService.register(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtAuthentication> login(@RequestBody LoginRequest request) {
    JwtAuthentication response = userService.login(request);
    return ResponseEntity.ok(response);
  }
}
