package org.luk.task_manager_api.repository;

import java.util.Optional;

import org.luk.task_manager_api.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
  Optional<Project> findByTitle(String title);

  boolean existsByTitle(String title);
}
