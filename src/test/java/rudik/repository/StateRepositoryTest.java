package rudik.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rudik.configartion.AbstractTestContainers;
import rudik.model.State;
import static org.assertj.core.api.Assertions.fail;
import java.util.ArrayList;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)   //Учтите, что при тестировании с помощью @DataJpaTest используются встроенные СУБД в оперативной памяти. Для тестирования же с настоящей базой данных необходимо снабдить класс теста аннотацией @AutoConfigureTestDatabase(replace=Replace.NONE).
public class StateRepositoryTest extends AbstractTestContainers {

    @Autowired
    private StateRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    private State state;

    @BeforeEach
    public void setUp() {
        this.state = new State();
        state.setName("State");
        state.setTasks(new ArrayList<>());
    }

    @Test
    public void shouldFindAll() {
        testEntityManager.persist(state);
        Assertions.assertEquals(5, repository.getAll().size());
    }

    @Test
    public void shouldCreate() {
        repository.save(state);
        Assertions.assertEquals(state, repository.getOne(state.getId()));
    }

    @Test
    public void shouldReadById() {
        Assertions.assertEquals(5L, repository.findById(5L)
                .orElseThrow(() -> new TestAbortedException("Error whi;e findById")).getId());
    }

    @Test
    public void shouldUpdate() {
        testEntityManager.persist(state);
        //Сделайте экземпляр сущности управляемым и постоянным.
        // Как мы помним, в основе JPA лежит понятие контекст персистенции (Persistence Context). Это место, где живут сущности.
        // А мы управляем сущностями через EntityManager.
        // Когда мы выполняем комманду persist, то мы помещаем сущность в контекст. Точнее, мы говорим EntityManager'у, что это нужно сделать.
        state.setName("Name");
        repository.save(state);
        Assertions.assertEquals(state.getName(), repository.findById(state.getId())
                .orElseThrow(() -> new TestAbortedException("Error while findById()")).getName());
    }

    @Test
    public void shouldDelete() {
        Assertions.assertNotNull(repository.getOne(5L));
        repository.deleteById(5L);
        repository.findById(5L).ifPresent(a->fail("Exists after delete"));
    }

    @Test
    public void shouldGetByName(){
        Assertions.assertEquals(repository.getOne(5L),
                repository.getByName(repository.getOne(5L).getName()));
    }

    @Test
    public void shouldGetAll(){
        shouldFindAll();
    }
}