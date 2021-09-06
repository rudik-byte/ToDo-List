package rudik.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rudik.exception.NullEntityReferenceException;
import rudik.model.User;
import rudik.repository.UserRepository;
import rudik.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) throws NullEntityReferenceException {
        try {
            return userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new NullEntityReferenceException("User cannot be 'null'");
        }
    }

    @Override
    public User readById(long id) throws EntityNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new EntityNotFoundException("User with id " + id + " not found");
    }

    @Override
    @Transactional
    public User update(User user) throws NullEntityReferenceException {
        //User oldUser = readById(user.getId());
        try {
            return userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new NullEntityReferenceException("User cannot be 'null'");
        }

    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        User user = readById(id);
        userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        List<User> userList = userRepository.findAll();
        return userList.isEmpty() ? new ArrayList<>() : userList;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User not Found!");
        }
        return user;
    }
}

