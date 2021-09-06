package rudik.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import rudik.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User create(User user);

    User readById(long id);

    User update(User user);

    void delete(long id);

    List<User> getAll();
}
