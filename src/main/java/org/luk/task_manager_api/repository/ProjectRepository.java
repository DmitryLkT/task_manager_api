package org.luk.task_manager_api.repository;

import java.util.List;
import java.util.Optional;

import org.luk.task_manager_api.model.Project;
import org.luk.task_manager_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
  Optional<Project> findByTitle(String title);

  List<Project> findByOwner(User owner);

  boolean existsByTitle(String title);
}
