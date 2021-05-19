package service;

import exception.EntityNotFoundException;
import exception.NullEntityReferenceException;
import model.User;

import java.util.List;

public interface UserService {
    User create(User user) throws NullEntityReferenceException;
    User readById(long id) throws EntityNotFoundException;
    User update(User user) throws EntityNotFoundException, NullEntityReferenceException;
    void delete(long id) throws EntityNotFoundException;
    List<User> getAll();
}
