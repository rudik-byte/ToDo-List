package service;

import exception.EntityNotFoundException;
import exception.NullEntityReferenceException;
import model.ToDo;

import java.util.List;

public interface ToDoService {
    ToDo create(ToDo todo) throws NullEntityReferenceException;
    ToDo readById(long id) throws EntityNotFoundException;
    ToDo update(ToDo todo) throws EntityNotFoundException, NullEntityReferenceException;
    void delete(long id) throws EntityNotFoundException;

    List<ToDo> getAll();
    List<ToDo> getByUserId(long userId);
}
