import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.Getter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="roles")
public class Role {

  public enum RoleType {
    ROLE_USER,
    ROLE_ADMIN
  }

  @Id
  @Column(name="id")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name="name", nullable=false)
  private RoleType name;
  
  @ManyToMany(mappedBy="roles", fetch=FetchType.LAZY)
  private Set<User> users = new HashSet<>();

  public Role(String name) {
    this.name = name;
  }
}