package rudik.service;

import rudik.exception.EntityNotFoundException;
import rudik.exception.NullEntityReferenceException;
import rudik.model.Role;

import java.util.List;

public interface RoleService {
    Role create(Role role) throws NullEntityReferenceException;
    Role readById(long id) throws EntityNotFoundException;
    Role update(Role role) throws EntityNotFoundException, NullEntityReferenceException;
    void delete(long id) throws EntityNotFoundException;
    List<Role> getAll();
}
