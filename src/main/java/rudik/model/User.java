package rudik.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User implements UserDetails {

    private static final String REGEX_FOR_NAME = "[A-Z][a-z]+";
    private static final String REGEX_FOR_EMAIL = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}";
    private static final String REGEX_FOR_PASSWORD = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = REGEX_FOR_NAME,
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Pattern(regexp = REGEX_FOR_NAME,
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Pattern(regexp = REGEX_FOR_EMAIL,
            message = "Must be a valid e-mail address: exemple@exemple.com")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Pattern(regexp = REGEX_FOR_PASSWORD,
            message = "Must be a minimum 6 charters, at least one letter and one number")
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<ToDo> myTodos;

    @ManyToMany
    @JoinTable(name = "todo_collaborator",
            joinColumns = @JoinColumn(name = "collaborator_id"),
            inverseJoinColumns = @JoinColumn(name = "todo_id"))
    private List<ToDo> otherTodos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
