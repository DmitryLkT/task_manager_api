package org.luk.task_manager_api.config; 

import java.util.Set;
import java.util.Collection;

import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.model.Role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record CustomUserDetails(User user) implements UserDetails {

  //Возвращает предоставленные пользователю полномочия.
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoles().stream()
              .map(role -> new SimpleGrantedAuthority(role.getName()))
              .toList();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  public Long getId() {
    return user.getId();
  }

  public Set<Role> getRoles() {
    return user.getRoles();
  }

  //Указывает, истёк ли аккаунт пользователя.
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  //Указывает, заблокирован ли пользователь или разблокирован.
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  //Указывает, истёк ли срок действия учетных данных пользователя (пароль).
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  //Указывает, включён ли пользователь или отключён.
  @Override
    public boolean isEnabled() {
        return true;
    }
}