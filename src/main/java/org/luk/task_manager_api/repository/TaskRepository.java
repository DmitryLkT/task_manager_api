package org.luk.task_manager_api.repository;

import java.util.List;

import org.luk.task_manager_api.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByProjectId(Long id);

  List<Task> findByUserId(Long id);
}
