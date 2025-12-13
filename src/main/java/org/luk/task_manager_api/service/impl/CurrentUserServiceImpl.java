package org.luk.task_manager_api.service.impl;


import org.luk.task_manager_api.model.Project;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.repository.UserRepository;
import org.luk.task_manager_api.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {
  private final UserRepository userRepository;

  @Override
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if(authentication == null || !authentication.isAuthenticated()) {
        throw new RuntimeException("User not authenticated");
      }

    String email = authentication.getName();

    return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
  
  @Override
  public boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
  }

  @Override
  public void checkProjectOwnership(Project project, User user) {
    if(!project.getOwner().getId().equals(user.getId())) {
      throw new RuntimeException("Access denied to project: " + project.getTitle());
    }
  }
}
