package org.luk.task_manager_api.model;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="users")
public class User {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(name="name", nullable=false)
  private String name;

  @Column(name="email", unique=true, nullable=false)
  private String email;

  @Column(name="password_hash", nullable=false, length=255)
  private String password;

  @ManyToMany(cascade=CascadeType.PERSIST)
  @JoinTable(
    name="user_roles",
    joinColumns=@JoinColumn(name="user_id"),
    inverseJoinColumns=@JoinColumn(name="role_id") 
  )
  private Set<Role> roles = new HashSet<>();

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public void addRolesToUser(Role role) {
    this.roles.add(role);
    role.getUsers().add(this);
  }

  public void removeRolesToUser(Role role) {
    this.roles.remove(role);
    role.getUsers().remove(this);
  }
}