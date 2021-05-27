package rudik.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rudik.exception.NullEntityReferenceException;
import rudik.model.User;
import rudik.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private static User user;

    @BeforeAll
    public static void init() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Mikegdsfgs414");
        user.setLastName("Pavliuk");
        user.setLastName("Pavliuk");
        user.setEmail("mykhailo@gmail.com");
        user.setPassword("Qwerty123");
    }

    @Test
    public void createValidUserTest() throws NullEntityReferenceException {
        userService.create(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void createNullUserTest() throws NullEntityReferenceException {
        Mockito.when(userService.create(null)).thenThrow(new IllegalArgumentException());
        Assertions.assertThrows(NullEntityReferenceException.class,
                ()-> userService.create(null));
    }

    @Test
    public void updateValidUserTest() throws NullEntityReferenceException, rudik.exception.EntityNotFoundException {
        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findById(user.getId());

        userService.update(user);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void updateNullUserTest() {
        Assertions.assertThrows(NullEntityReferenceException.class,
                ()-> userService.update(null));
    }

    @Test
    public void deleteExistingUserTest() throws rudik.exception.EntityNotFoundException {
        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findById(user.getId());

        userService.delete(1L);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    public void deleteNotExistingUserTest() {
        Mockito.when(userRepository
                .findById(user.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.delete(user.getId()));
    }

    @Test
    public void getAllUsersTest() throws NullEntityReferenceException {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userService.create(user);
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        Assertions.assertEquals(userService.getAll(), userList);
    }
}
