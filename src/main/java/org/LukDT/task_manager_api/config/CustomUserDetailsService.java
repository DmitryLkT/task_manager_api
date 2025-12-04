package org.LukDT.task_manager_api.config; 

import org.LukDT.task_manager_api.model.User;
import org.LukDT.task_manager_api.repository.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  @Transactional
  public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email) 
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    return new CustomUserDetails(user);
  }
}