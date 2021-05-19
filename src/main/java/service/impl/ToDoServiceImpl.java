package service.impl;

import exception.EntityNotFoundException;
import exception.NullEntityReferenceException;
import model.ToDo;
import org.springframework.stereotype.Service;
import repository.ToDoRepository;
import service.ToDoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoServiceImpl implements ToDoService {

    private ToDoRepository toDoRepository;

    public ToDoServiceImpl(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

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

        return optional.orElseThrow(() -> new EntityNotFoundException("ToDo with id =" + id + " does not exist!"));
    }

    @Override
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
