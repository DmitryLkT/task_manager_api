package org.luk.task_manager_api.service;

import org.luk.task_manager_api.model.Project;
import org.luk.task_manager_api.model.User;

public interface CurrentUserService {
  User getCurrentUser();

  void checkProjectOwnership(Project project, User user);
}