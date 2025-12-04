package org.luk.task_manager_api.repository;

import java.util.Optional;

import org.luk.task_manager_api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  //Используется, при аутентификации: нужно получить пользователя по email.
  Optional<User> findByEmail(String email);

  //Автоматически задает вопрос, есть ли хотя бы один пользователь с таким email
  boolean existsByEmail(String email);
}
