import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="user")
public class User {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(name="name", nullable=true)
  private String name;

  @Column(name="email", unique=true, nullable=true)
  private String email;

  @Column(name="password_hash", unique=true, nullable=true)
  private String password;

  @ManyToMany(cascade=CascadeType.ALL)
  @JoinTable(
    name="user_role",
    joinColumns=@JoinColumn(name="roles_id"),
    inverseJoinColumns=@JoinColumn(name="user_id") 
  )
  private Set<Role> roles;

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
    roles = new HashSet<>();
  }

  public void addRolesToUser(Role role) {
    this.roles.add(role);
    role.getUser().add(this);
  }

  public void removeRolesToUser(Role role) {
    this.roles.remove(role);
    role.getUser().remove(this);
  }
}
