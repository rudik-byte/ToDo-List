package rudik.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rudik.dto.TaskDTO;
import rudik.dto.TaskTransformer;
import rudik.exception.EntityNotFoundException;
import rudik.exception.NullEntityReferenceException;
import rudik.model.Priority;
import rudik.model.Task;
import rudik.service.StateService;
import rudik.service.TaskService;
import rudik.service.ToDoService;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ToDoService todoService;
    private final StateService stateService;

    public TaskController(TaskService taskService, ToDoService todoService, StateService stateService) {
        this.taskService = taskService;
        this.todoService = todoService;
        this.stateService = stateService;
    }

    @GetMapping("/create/todos/{todo_id}")
    public String create(@PathVariable("todo_id") long todoId, Model model) throws EntityNotFoundException, EntityNotFoundException {
        model.addAttribute("task", new TaskDTO());
        model.addAttribute("todo", todoService.readById(todoId));
        model.addAttribute("priorities", Priority.values());
        return "create-task";
    }

    @PostMapping("/create/todos/{todo_id}")
    public String create(@PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task") TaskDTO taskDto, BindingResult result) throws EntityNotFoundException, NullEntityReferenceException, NullEntityReferenceException {
        if (result.hasErrors()) {
            model.addAttribute("todo", todoService.readById(todoId));
            model.addAttribute("priorities", Priority.values());
            return "create-task";
        }
        Task task = TaskTransformer.convertToEntity(
                taskDto,
                todoService.readById(taskDto.getTodoId()),
                stateService.getByName("New")
        );
        taskService.create(task);
        return "redirect:/todos/" + todoId + "/tasks";
    }

    @GetMapping("/{task_id}/update/todos/{todo_id}")
    public String update(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId, Model model) throws EntityNotFoundException {
        TaskDTO taskDto = TaskTransformer.convertToDTO(taskService.readById(taskId));
        model.addAttribute("task", taskDto);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("states", stateService.getAll());
        return "update-task";
    }

    @PostMapping("/{task_id}/update/todos/{todo_id}")
    public String update(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task") TaskDTO taskDto, BindingResult result) throws EntityNotFoundException, NullEntityReferenceException {
        if (result.hasErrors()) {
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("states", stateService.getAll());
            return "update-task";
        }
        Task task = TaskTransformer.convertToEntity(
                taskDto,
                todoService.readById(taskDto.getTodoId()),
                stateService.readById(taskDto.getStateId())
        );
        taskService.update(task);
        return "redirect:/todos/" + todoId + "/tasks";
    }

    @GetMapping("/{task_id}/delete/todos/{todo_id}")
    public String delete(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId) throws EntityNotFoundException {
        taskService.delete(taskId);
        return "redirect:/todos/" + todoId + "/tasks";
    }
}
