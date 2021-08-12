package rudik.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rudik.exception.NullEntityReferenceException;
import rudik.model.ToDo;
import rudik.repository.ToDoRepository;
import rudik.service.ToDoService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;

    @Override
    public ToDo create(ToDo todo) throws NullEntityReferenceException {
        if (!todo.getTitle().isEmpty()) {
            return toDoRepository.save(todo);
        }
        throw new NullEntityReferenceException("ToDo cannot be 'null'!");
    }

    @Override
    public ToDo readById(long id) throws EntityNotFoundException {
        Optional<ToDo> optional = toDoRepository.findById(id);

        return optional.orElseThrow(() -> new EntityNotFoundException("ToDo with id = " + id + " does not exist!"));
    }

    @Override
    @Transactional
    public ToDo update(ToDo todo) throws EntityNotFoundException, NullEntityReferenceException {
        if (!todo.getTitle().isEmpty()) {
            ToDo oldTodo = readById(todo.getId());
            return toDoRepository.save(todo);
        }
        throw new NullEntityReferenceException("ToDo cannot be 'null'!");
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        ToDo toDo = readById(id);
        toDoRepository.delete(toDo);
    }

    @Override
    public List<ToDo> getAll() {
        List<ToDo> toDos = toDoRepository.findAll();
        return toDos.isEmpty() ? new ArrayList<>() : toDos;
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        List<ToDo> toDos = toDoRepository.getByUserId(userId);
        return toDos.isEmpty() ? new ArrayList<>() : toDos;
    }
}
