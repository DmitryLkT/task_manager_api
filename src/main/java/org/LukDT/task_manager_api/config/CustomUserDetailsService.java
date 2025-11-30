import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email) 
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    return new CustomUserDetails(user);
  }
  
}