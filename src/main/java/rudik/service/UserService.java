package rudik.service;

import rudik.exception.EntityNotFoundException;
import rudik.exception.NullEntityReferenceException;
import rudik.model.User;

import java.util.List;

public interface UserService {
    User create(User user) throws NullEntityReferenceException;
    User readById(long id) throws EntityNotFoundException;
    User update(User user) throws EntityNotFoundException, NullEntityReferenceException;
    void delete(long id) throws EntityNotFoundException;
    List<User> getAll();
}
