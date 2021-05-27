package rudik.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rudik.exception.NullEntityReferenceException;
import rudik.model.Priority;
import rudik.model.Task;
import rudik.repository.TaskRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TaskServiceTest {

    @Autowired
    private TaskService service;

    @MockBean
    private TaskRepository repository;

    @Test
    void create() throws NullEntityReferenceException {
        Task task = new Task();
        task.setPriority(Priority.LOW);
        task.setName("Example name");

        Mockito.doReturn(task)
                .when(repository)
                .save(task);

        Task created = service.create(task);

        Assertions.assertSame(task, created);
        Mockito.verify(repository, Mockito.times(1)).save(task);
    }

    @Test
    void createFailed() {
        Task task = null;

        Mockito.when(repository.save(task))
                .thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(NullEntityReferenceException.class, () -> service.create(task));
        Mockito.verify(repository, Mockito.times(1)).save(task);
    }

    @Test
    void readById() throws rudik.exception.EntityNotFoundException {
        Task task = new Task();
        task.setId(36);

        Mockito.doReturn(Optional.of(task))
                .when(repository)
                .findById(task.getId());

        Assertions.assertEquals(task, service.readById(task.getId()));
        Mockito.verify(repository, Mockito.times(1)).findById(task.getId());
    }

    @Test
    void readByIdFailed() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readById(25L));
        Mockito.verify(repository, Mockito.times(1)).findById(25L);
    }

    @Test
    void update() throws NullEntityReferenceException, rudik.exception.EntityNotFoundException {
        Task task = new Task();
        task.setId(85);

        task.setName("New Name");

        Mockito.doReturn(Optional.of(task))
                .when(repository)
                .findById(task.getId());

        service.update(task);

        Assertions.assertEquals("New Name", task.getName());
        Mockito.verify(repository, Mockito.times(1)).save(task);
    }

    @Test
    void updateFailed() {
        Task task = null;
        Assertions.assertThrows(NullEntityReferenceException.class, () -> service.update(task));
        Mockito.verify(repository, Mockito.times(0)).save(task);
    }

    @Test
    void delete() throws rudik.exception.EntityNotFoundException {
        Task task = new Task();
        task.setId(15);

        Mockito.doReturn(Optional.of(task))
                .when(repository)
                .findById(task.getId());

        service.delete(task.getId());

        Mockito.verify(repository, Mockito.times(1)).delete(task);
    }

    @Test
    void deleteFailed() {
        Task task = new Task();
        task.setId(15);

        Mockito.when(repository.findById(task.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.delete(task.getId()));
        Mockito.verify(repository, Mockito.times(1)).findById(task.getId());
    }

    @Test
    void getAll() throws NullEntityReferenceException {
        List<Task> tasks = Arrays.asList(new Task(), new Task(), new Task());
        for (int i = 0; i <tasks.size(); i++) {
            service.create(tasks.get(i));
        }                                     //ловлю ошибку   tasks.forEach(service::create); ???
        Mockito.doReturn(tasks)
                .when(repository)
                .findAll();

        Assertions.assertEquals(tasks, service.getAll());

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void getByTodoId() {
        int todoId = 72;
        Task task1 = new Task();
        Task task2 = new Task();

        Mockito.doReturn(Arrays.asList(task1, task2))
                .when(repository)
                .getByTodoId(todoId);

        Assertions.assertEquals(Arrays.asList(task1, task2), service.getByTodoId(todoId));
        Mockito.verify(repository, Mockito.times(1)).getByTodoId(todoId);
    }
}
