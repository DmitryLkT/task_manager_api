package org.luk.task_manager_api.service.adminServices.interfaceService;

import java.util.List;

import org.luk.task_manager_api.dto.UserResponse;

public interface UserAdminService {
  List<UserResponse> getAllUser();

  UserResponse getUserById(Long id);

  void updateUserRole(Long userId, Long roleId);
}
