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
import rudik.model.State;
import rudik.model.Task;
import rudik.repository.StateRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StateServiceTest {
    @Autowired
    private StateService service;

    @MockBean
    private StateRepository repository;

    @Test
    void create() {

        State state = new State();
        state.setName("State Name");
        state.setTasks(Arrays.asList(new Task(), new Task()));

        Mockito.doReturn(state)
                .when(repository)
                .save(state);

        State created = service.create(state);
        Assertions.assertSame(created, state);
        Mockito.verify(repository, Mockito.times(1)).save(state);
    }

    @Test
    void createFailed() {
        State state = null;

        Mockito.when(repository.save(state))
                .thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(NullEntityReferenceException.class, () -> service.create(state));
        Mockito.verify(repository, Mockito.times(1)).save(state);
    }

    @Test
    void readById() throws rudik.exception.EntityNotFoundException {
        State state = new State();

        Mockito.doReturn(Optional.of(state))
                .when(repository)
                .findById(state.getId());


        Assertions.assertEquals(state, service.readById(state.getId()));
        Mockito.verify(repository, Mockito.times(1)).findById(state.getId());
    }

    @Test
    void readByIdFailed() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readById(25L));
        Mockito.verify(repository, Mockito.times(1)).findById(25L);
    }

    @Test
    void update() throws rudik.exception.EntityNotFoundException {
        State state = new State();

        state.setName("New Name");

        Mockito.doReturn(Optional.of(state))
                .when(repository)
                .findById(state.getId());

        service.update(state);

        Assertions.assertEquals("New Name", state.getName());
        Mockito.verify(repository, Mockito.times(1)).save(state);
    }

    @Test
    void updateFailed() {
        State state = null;
        Assertions.assertThrows(NullEntityReferenceException.class, () -> service.update(state));
        Mockito.verify(repository, Mockito.times(0)).save(state);
    }

    @Test
    void delete() throws rudik.exception.EntityNotFoundException {
        State state = new State();

        Mockito.doReturn(Optional.of(state))
                .when(repository)
                .findById(state.getId());

        service.delete(state.getId());

        Mockito.verify(repository, Mockito.times(1)).delete(state);
    }

    @Test
    void deleteFailed() {
        State state = new State();

        Mockito.when(repository.findById(state.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.delete(state.getId()));
        Mockito.verify(repository, Mockito.times(1)).findById(state.getId());
    }

    @Test
    void getByName() throws rudik.exception.EntityNotFoundException {
        State state = new State();
        state.setName("State Name");

        Mockito.when(repository.getByName(state.getName()))
                .thenReturn(state);

        Assertions.assertEquals(state, service.getByName(state.getName()));
        Mockito.verify(repository, Mockito.times(1)).getByName(state.getName());
    }

    @Test
    void getByNameFailed() {

        Mockito.when(repository.getByName(Mockito.any()))
                .thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getByName("Any Name"));
        Mockito.verify(repository, Mockito.times(1)).getByName("Any Name");
    }

    @Test
    void getAll() {
        List<State> expected = Arrays.asList(new State(), new State(), new State());

        Mockito.when(repository.getAll())
                .thenReturn(expected);

        Assertions.assertEquals(expected, service.getAll());
        Mockito.verify(repository, Mockito.times(1)).getAll();

    }
}
