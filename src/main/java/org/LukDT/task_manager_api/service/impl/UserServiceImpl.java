import org.LukDT.task_manager_api.repository.*;

import java.util.stream.Collectors;

import org.LukDT.task_manager_api.config.customUserDetailsService;
import org.LukDT.task_manager_api.config.jwt.JwtService;

import org.apache.catalina.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final CustomUserDetailsService customUserDetailsService;
  private final JwtService jwtService;
  
  @Override
  public UserResponse register(RegisterRequest request) {
    if(userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
    user.addRolesToUser(role);
    
    userRepository.save(user);

    return mapToUserResponse(user);
  }

  @Override
  public JwtAuthentication login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
                 .orElseThrow(() -> new RuntimeException("User not found"));

    if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new RuntimeException("Invalid password");
    }
    UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

    String accessToken = jwtService.generateToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    return new JwtAuthentication(accessToken, refreshToken, mapToUserResponse(user));
  }

  @Override
  public UserResponse getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    String email = auth.getName();

    User user = userRepository.findByEmail(request.getEmail())
                 .orElseThrow(() -> new RuntimeException("User not found"));

    return mapToUserResponse(user);
  }

  private UserResponse mapToUserResponse(User user) {
    Set<RoleResponse> roles = user.getRoles().stream()
                .map(r -> new RoleResponse(r.getId(), r.getName()))
                .collect(Collectors.toSet());
    return new UserResponse(user.getId(), user.getEmail(), user.getName(), roles);            
  }
}
