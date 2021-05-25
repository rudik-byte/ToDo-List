package rudik.repository;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rudik.model.Priority;
import rudik.model.Task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Task task;

    @BeforeEach
    public void testTask(){
        this.task = new Task();
        task.setName("Task");
        task.setPriority(Priority.LOW);
    }

    @Test
    public void shouldFindAll(){
        Task task1 = new Task();
        task1.setName("Task1");
        task1.setPriority(Priority.LOW);
        entityManager.persist(task1);
        Task task2 = new Task();
        task2.setName("Task2");
        task2.setPriority(Priority.MEDIUM);
        entityManager.persist(task2);
        Iterable<Task> allTasks = taskRepository.findAll();
        assertThat(allTasks).hasSize(5).contains(task1,task2);
    }

    @Test
    public void shouldCreate(){
        entityManager.persist(task);
        Task newTask = taskRepository.findById(task.getId()).
                orElseThrow(()->new TestAbortedException("Error while findById"));
        Assertions.assertEquals(task,newTask);
    }

    @Test
    public void shouldReadById(){
        Assertions.assertEquals("Task #1", taskRepository.getOne(5L).getName());
    }

    @Test
    public void shouldUpdate(){
        entityManager.persist(task);
        Task update = new Task();
        update.setName("UpdatedTask");
        update.setPriority(Priority.MEDIUM);
        update.setId(task.getId());
        update = taskRepository.save(update);
        Assertions.assertEquals(task,update);
    }

    @Test
    public void shouldDelete(){
        Assertions.assertNotNull(taskRepository.getOne(5L));
        taskRepository.delete(taskRepository.getOne(5L));
        taskRepository.findById(5L).ifPresent(a->fail("Exists after delete"));
    }

    @Test
    public void shouldDeleteById(){
        Assertions.assertNotNull(taskRepository.getOne(5L));
        taskRepository.deleteById(5L);
        taskRepository.findById(5L).ifPresent(a -> fail("Exists after delete by ID"));
    }

    @Test
    public void shouldGetByToDoId(){
        Assertions.assertEquals(3,taskRepository.getByTodoId(7).size());
    }

    @Test
    public void shouldDeleteAll(){
        taskRepository.deleteAll();
        Assertions.assertEquals(0,taskRepository.findAll().size());
    }
}
