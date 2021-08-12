package rudik.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import rudik.exception.NullEntityReferenceException;
import rudik.model.ToDo;
import rudik.model.User;
import rudik.repository.ToDoRepository;
import rudik.service.impl.ToDoServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ToDoServiceTest {
    @Mock
    private ToDoRepository toDoRepository;

    @InjectMocks
    private ToDoServiceImpl toDoService;

    public ToDo expected;

    final Long TODO_ID = 1L;
    final Long USER_ID = 2L;
    final String TITLE = "title";

    @BeforeEach
    void setUp() {
        expected = new ToDo();
        User user = new User();
        user.setId(USER_ID);

        expected.setId(TODO_ID);
        expected.setTitle(TITLE);
        expected.setOwner(user);
    }

    @Test
    public void shouldCreatToDo() {
        Mockito.doReturn(expected).when(toDoRepository).save(expected);

        ToDo actual = toDoService.create(expected);

        verify(toDoRepository).save(any(ToDo.class));
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void shouldThrowNullEntity() {
        Mockito.doThrow(new RuntimeException()).when(toDoRepository).save(expected);

        Exception exception = assertThrows(NullEntityReferenceException.class, () -> {
            toDoService.create(null);
        });

        Assertions.assertEquals("To-Do cannot be 'null'", exception.getMessage());
    }

    @Test
    public void shouldReadByiId() {
        // Mockito.doReturn(toDoRepository.findById(anyLong())).when(toDoRepository).save(expected);
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        ToDo actual = toDoService.readById(TODO_ID);

        verify(toDoRepository).findById(anyLong());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void shouldThrowExceptionReadById() {
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            toDoService.readById(TODO_ID);
        });

        Assertions.assertEquals("To-Do with id " + TODO_ID + " not found", exception.getMessage());
    }

    @Test
    public void shouldUpdateToDo() {
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        Mockito.doReturn(expected).when(toDoRepository).save(expected);

        ToDo actual = toDoService.update(expected);

        verify(toDoRepository).save(any(ToDo.class));
        verify(toDoRepository).findById(anyLong());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void shouldThrowNullUpdateToDo(){
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> {
            toDoService.update(null);
        });

        Assertions.assertEquals("To-Do cannot be 'null'", exception.getMessage());
        verifyNoInteractions(toDoRepository);
    }

    @Test
    public void shouldDeleteToDo(){
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        toDoService.delete(TODO_ID);

        verify(toDoRepository).findById(anyLong());
        verify(toDoRepository).delete(any(ToDo.class));
    }

    @Test
    public void shouldNotUpdateToDo(){
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            toDoService.update(expected);
        });

        Assertions.assertEquals("To-Do with id 1 not found", exception.getMessage());
        verify(toDoRepository, times(0)).save(any(ToDo.class));
    }

    @Test
    public void shouldThrowDeleteToDo(){
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            toDoService.delete(TODO_ID);
        });

        Assertions.assertEquals("To-Do with id " + TODO_ID + " not found", exception.getMessage());
        verify(toDoRepository).findById(anyLong());
        verify(toDoRepository, times(0)).delete(any(ToDo.class));
    }

    @Test
    public void shouldReturnNewArrayListGetAll(){
        when(toDoRepository.findAll()).thenReturn(Collections.emptyList());

        List<ToDo> list = toDoService.getAll();

        Assertions.assertEquals(0,list.size());
        verify(toDoRepository).findAll();
    }

    @Test
    public void shouldReturnListToDoGetAll(){
        List<ToDo>expected = Arrays.asList(new ToDo(), new ToDo());
        Mockito.doReturn(expected).when(toDoRepository).findAll();

        List<ToDo>actual = toDoService.getAll();

        Assertions.assertEquals(expected.size(),actual.size());
        verify(toDoRepository).findAll();
    }

    @Test
    public void shouldReturnListGetByUserId(){
        when(toDoRepository.getByUserId(anyLong())).thenReturn(Collections.emptyList());

        List<ToDo> actual = toDoService.getByUserId(USER_ID);

        Assertions.assertEquals(0, actual.size());
        verify(toDoRepository).getByUserId(anyLong());
    }

    @Test
    public void shouldReturnListToDoGetByUserId(){
        List<ToDo> exp = Arrays.asList(new ToDo(), new ToDo());

        when(toDoRepository.getByUserId(anyLong())).thenReturn(exp);

        List<ToDo> act = toDoService.getByUserId(USER_ID);

        verify(toDoRepository).getByUserId(anyLong());
        Assertions.assertEquals(exp,act);
    }
}
