package org.luk.task_manager_api.service.impl;

import org.luk.task_manager_api.exception.customExceptions.AccessDeniedException;
import org.luk.task_manager_api.exception.customExceptions.UserNotAuthenticatedException;
import org.luk.task_manager_api.exception.customExceptions.UserNotFoundException;
import org.luk.task_manager_api.model.Project;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.repository.UserRepository;
import org.luk.task_manager_api.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        throw new UserNotAuthenticatedException();
      }

    String email = authentication.getName();

    return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
  }
  
  @Override
  public void checkProjectOwnership(Project project, User user) {
    if(!project.getOwner().getId().equals(user.getId())) {
      throw new AccessDeniedException(String.format("Access denied to project: %s", project.getTitle()));
    }
  }
}
