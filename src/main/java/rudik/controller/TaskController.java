package rudik.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rudik.dto.TaskDTO;
import rudik.dto.TaskTransformer;
import rudik.exception.NullEntityReferenceException;
import rudik.model.Priority;
import rudik.model.Task;
import rudik.service.StateService;
import rudik.service.TaskService;
import rudik.service.ToDoService;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;
    private final ToDoService todoService;
    private final StateService stateService;


    @GetMapping("/create/todos/{todo_id}")
    @ApiOperation("Create new Task")
    public String create(@PathVariable("todo_id") long todoId, Model model) throws EntityNotFoundException {
        LOG.info("GET /tasks/create/todos/" + todoId);
        model.addAttribute("task", new TaskDTO());
        model.addAttribute("todo", todoService.readById(todoId));
        model.addAttribute("priorities", Priority.values());
        return "create-task";
    }

    @PostMapping("/create/todos/{todo_id}")
    @ApiOperation("Create task")
    public String create(@PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task") TaskDTO taskDto, BindingResult result) throws EntityNotFoundException, NullEntityReferenceException {
        LOG.info("POST /tasks/create/todos/" + todoId);
        if (result.hasErrors()) {
            LOG.info("POST /tasks/create/todo/" + todoId + " | Has errors");
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
    @ApiOperation("Update Task")
    public String update(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId, Model model) throws EntityNotFoundException {
        LOG.info("GET /tasks/" + taskId + "/update/todos/" + todoId);
        TaskDTO taskDto = TaskTransformer.convertToDTO(taskService.readById(taskId));
        model.addAttribute("task", taskDto);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("states", stateService.getAll());
        return "update-task";
    }

    @PostMapping("/{task_id}/update/todos/{todo_id}")
    @ApiOperation("Update task")
    public String update(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task") TaskDTO taskDto, BindingResult result) throws EntityNotFoundException, NullEntityReferenceException {
        LOG.info("POST /tasks/" + taskId + "/update/todos/" + todoId);
        if (result.hasErrors()) {
            LOG.info("POST /tasks/" + taskId + "/update/todos/" + todoId + " | Has error");
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

    @DeleteMapping("/{task_id}/delete/todos/{todo_id}")
    @ApiOperation("Delete Task")
    public String delete(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId) throws EntityNotFoundException {
        LOG.info("GET /tasks/" + taskId + "/delete/todos/" + todoId);
        taskService.delete(taskId);
        return "redirect:/todos/" + todoId + "/tasks";
    }
}
