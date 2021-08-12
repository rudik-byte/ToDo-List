package rudik.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rudik.exception.NullEntityReferenceException;
import rudik.model.Task;
import rudik.repository.TaskRepository;
import rudik.service.TaskService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task create(Task task) throws NullEntityReferenceException {
        if (!task.getName().isEmpty()) {
            return taskRepository.save(task);
        }

        throw new NullEntityReferenceException("Task can`t be a 'null'!");
    }

    @Override
    public Task readById(long id) throws EntityNotFoundException {
        Optional<Task> optional = taskRepository.findById(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("Task with this id = " + id + " does not exist!"));
    }

    @Override
    @Transactional
    public Task update(Task task) throws EntityNotFoundException, NullEntityReferenceException {
        if (!task.getName().isEmpty()) {
            //  Task oldTask = readById(task.getId());
            return taskRepository.save(task);
        }
        throw new NullEntityReferenceException("Task can`t be a 'null'!");
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        Task task = readById(id);
        taskRepository.delete(task);
    }

    @Override
    public List<Task> getAll() {
        List<Task> taskList = taskRepository.findAll();
        return taskList.isEmpty() ? new ArrayList<>() : taskList;
    }

    @Override
    public List<Task> getByTodoId(long todoId) {
        List<Task> taskList = taskRepository.getByTodoId(todoId);

        return taskList.isEmpty() ? new ArrayList<>() : taskList;
    }
}
