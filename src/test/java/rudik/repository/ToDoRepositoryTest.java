package rudik.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rudik.model.ToDo;
import rudik.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ToDoRepositoryTest {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository repository;


    @Test
    public void getByUserId(){
        ToDo toDo = new ToDo();
        toDo.setTitle("title2");
        toDo.setCreatedAt(LocalDateTime.now());

        ToDo toDo1 = new ToDo();
        toDo1.setTitle("title1");
        toDo1.setCreatedAt(LocalDateTime.now().minusDays(1));

        User user = new User();
        user.setRole(repository.findById(1L).get());
        user.setEmail("email@gmail.com");
        user.setFirstName("Valeriy");
        user.setLastName("Fantazer");
        user.setPassword("lolikF");
        user.setMyTodos(Arrays.asList(toDo,toDo1));

        toDo.setOwner(user);

        toDo1.setCollaborators(Arrays.asList(user));

        userRepository.save(user);
        toDoRepository.save(toDo);
        toDoRepository.save(toDo1);

        List<ToDo> expected = Arrays.asList(toDo,toDo1);

        List<ToDo> actual = toDoRepository.getByUserId(user.getId());

        Assertions.assertEquals(expected.size(),actual.size());
    }
}
