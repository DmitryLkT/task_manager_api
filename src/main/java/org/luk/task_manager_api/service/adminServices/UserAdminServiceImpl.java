package org.luk.task_manager_api.service.adminServices;

import java.util.List;
import java.util.stream.Collectors;

import org.luk.task_manager_api.dto.UserResponse;
import org.luk.task_manager_api.model.Role;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.repository.RoleRepository;
import org.luk.task_manager_api.repository.UserRepository;
import org.luk.task_manager_api.service.CurrentUserService;
import org.luk.task_manager_api.service.adminServices.interfaceService.UserAdminService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {
  private final CurrentUserService currentUserService;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  
  @Override
  @Transactional
  public List<UserResponse> getAllUser() {
    checkAdminAccess();

    return userRepository.findAll().stream()
                .map(u -> UserResponse.fromEntity(u))
                .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UserResponse getUserById(Long id) {
    checkAdminAccess();

    User user = userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    
    return UserResponse.fromEntity(user);    
  }

  @Override
  @Transactional
  public void updateUserRole(Long userId, Long roleId) {
    checkAdminAccess();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    Role role = roleRepository.findById(roleId)
        .orElseThrow(() -> new UsernameNotFoundException("Role not found with id: " + roleId));

    user.setRole(role);
  }


  private void checkAdminAccess() {
        if (!currentUserService.isAdmin()) {
      throw new RuntimeException("Admin role required");
    }
  }
}
