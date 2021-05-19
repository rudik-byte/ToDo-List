package service;

import exception.EntityNotFoundException;
import exception.NullEntityReferenceException;
import model.Task;

import java.util.List;

public interface TaskService {
    Task create(Task task) throws NullEntityReferenceException;
    Task readById(long id) throws EntityNotFoundException;
    Task update(Task task) throws EntityNotFoundException, NullEntityReferenceException;
    void delete(long id) throws EntityNotFoundException;

    List<Task> getAll();
    List<Task> getByTodoId(long todoId);

}
