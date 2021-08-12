package rudik.service.impl;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rudik.exception.NullEntityReferenceException;
import rudik.model.Role;
import rudik.repository.RoleRepository;
import rudik.service.RoleService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role create(Role role) throws NullEntityReferenceException {
        try {
            return roleRepository.save(role);
        } catch (IllegalArgumentException e) {
            throw new NullEntityReferenceException("Role cannot be 'null'");
        }
    }

    @Override
    public Role readById(long id) throws EntityNotFoundException {
        Optional<Role> optional = roleRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EntityNotFoundException("Role with this id = " + id + " not found");
    }

    @Override
    @Transactional
    public Role update(Role role) throws EntityNotFoundException, NullEntityReferenceException {
        // Role oldRole = readById(role.getId());
        try {
            return roleRepository.save(role);
        } catch (IllegalArgumentException e) {
            throw new NullEntityReferenceException("Role cannot be a 'null'");
        }
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        Role role = readById(id);
        roleRepository.delete(role);
    }

    @Override
    public List<Role> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.isEmpty() ? new ArrayList<>() : roles;
    }
}
