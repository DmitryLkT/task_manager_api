package org.luk.task_manager_api.repository;

import java.util.Optional;

import org.luk.task_manager_api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
  Optional<Role> findByName(String name);
}