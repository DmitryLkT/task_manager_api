package org.luk.task_manager_api.service.adminServices;

import java.util.List;
import java.util.stream.Collectors;

import org.luk.task_manager_api.dto.UserResponse;
import org.luk.task_manager_api.exception.customExceptions.RoleNotFoundException;
import org.luk.task_manager_api.exception.customExceptions.UserNotFoundException;
import org.luk.task_manager_api.model.Role;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.repository.RoleRepository;
import org.luk.task_manager_api.repository.UserRepository;
import org.luk.task_manager_api.service.adminServices.interfaceService.UserAdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  
  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponse> getAllUser() {
    return userRepository.findAll().stream()
                .map(u -> UserResponse.fromEntity(u))
                .collect(Collectors.toList());
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
    
    return UserResponse.fromEntity(user);    
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public void updateUserRole(Long userId, String role) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    Role findRole = roleRepository.findByName(role.trim().toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException(role));

    user.setRole(findRole);
  }
}
