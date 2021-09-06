package rudik.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rudik.exception.NullEntityReferenceException;
import rudik.model.Task;
import rudik.model.ToDo;
import rudik.model.User;
import rudik.security.WebAuthToken;
import rudik.service.TaskService;
import rudik.service.ToDoService;
import rudik.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService todoService;
    private final TaskService taskService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(ToDoController.class);

    @GetMapping("/create/users/{owner_id}")
    @ApiOperation("Create toDo")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.details.id == #ownerId")
    public String create(@PathVariable("owner_id") long ownerId, Model model) {
        logger.info("Creating toDo");
        model.addAttribute("todo", new ToDo());
        model.addAttribute("ownerId", ownerId);
        return "create-todo";
    }

    @PostMapping("/create/users/{owner_id}")
    @ApiOperation("Create toDo")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.details.id == #ownerId")
    public String create(@PathVariable("owner_id") long ownerId, @Validated @ModelAttribute("todo") ToDo todo, BindingResult result) throws NullEntityReferenceException, EntityNotFoundException {
        if (result.hasErrors()) {
            return "create-todo";
        }
        todo.setCreatedAt(LocalDateTime.now());
        todo.setOwner(userService.readById(ownerId));
        todoService.create(todo);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/{id}/tasks")
    @ApiOperation("Read toDo tasks")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @toDoController.canReadToDo(#id)")
    public String read(@PathVariable long id, Model model) throws EntityNotFoundException {
        logger.info("Read toDo id={}", id);
        ToDo todo = todoService.readById(id);
        List<Task> tasks = taskService.getByTodoId(id);
        List<User> users = userService.getAll().stream()
                .filter(user -> user.getId() != todo.getOwner().getId()).collect(Collectors.toList());
        model.addAttribute("todo", todo);
        model.addAttribute("tasks", tasks);
        model.addAttribute("users", users);
        return "todo-tasks";
    }

    @GetMapping("/{todo_id}/update/users/{owner_id}")
    @ApiOperation("Update toDo")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.details.id == #ownerId")
    public String update(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId, Model model) throws EntityNotFoundException {
        logger.info("Update toDo toDoId={}, ownerId={}", todoId, ownerId);
        ToDo todo = todoService.readById(todoId);
        model.addAttribute("todo", todo);
        return "update-todo";
    }

    @PostMapping("/{todo_id}/update/users/{owner_id}")
    @ApiOperation("Update toDo")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.details.id == #ownerId")
    public String update(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId,
                         @Validated @ModelAttribute("todo") ToDo todo, BindingResult result) throws EntityNotFoundException, NullEntityReferenceException {
        if (result.hasErrors()) {
            todo.setOwner(userService.readById(ownerId));
            return "update-todo";
        }
        ToDo oldTodo = todoService.readById(todoId);
        todo.setOwner(oldTodo.getOwner());
        todo.setCollaborators(oldTodo.getCollaborators());
        todoService.update(todo);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @DeleteMapping("/{todo_id}/delete/users/{owner_id}")
    @ApiOperation("Delete toDo")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.details.id == #ownerId")
    public String delete(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId) throws EntityNotFoundException {
        logger.info("Delete toDo toDoId={}, ownerId={}", todoId, ownerId);
        todoService.delete(todoId);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/all/users/{user_id}")
    @ApiOperation("Get all toDo")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.details.id == #userId")
    public String getAll(@PathVariable("user_id") long userId, Model model) throws EntityNotFoundException {
        logger.info("GetAll toDo userId={}", userId);
        List<ToDo> todos = todoService.getByUserId(userId);
        model.addAttribute("todos", todos);
        model.addAttribute("user", userService.readById(userId));
        return "todos-user";
    }

    @GetMapping("/{id}/add")
    @ApiOperation("Add collaborator for toDo")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') " +
            "and authentication.details.id == @toDoServiceImpl.readById(#id).owner.id")
    public String addCollaborator(@PathVariable long id, @RequestParam("user_id") long userId) throws EntityNotFoundException, NullEntityReferenceException {
        logger.info("AddCollaborator toDoId = {}, userId={}", id, userId);
        ToDo todo = todoService.readById(id);
        List<User> collaborators = todo.getCollaborators();
        collaborators.add(userService.readById(userId));
        todo.setCollaborators(collaborators);
        todoService.update(todo);
        return "redirect:/todos/" + id + "/tasks";
    }

    @GetMapping("/{id}/remove")
    @ApiOperation("Remove Collaborator")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') " +
            "and authentication.details.id == @toDoServiceImpl.readById(#id).owner.id")
    public String removeCollaborator(@PathVariable long id, @RequestParam("user_id") long userId) throws EntityNotFoundException, NullEntityReferenceException {
        logger.info("RemoveCollaborator toDoId = {}, userId={}", id, userId);
        ToDo todo = todoService.readById(id);
        List<User> collaborators = todo.getCollaborators();
        collaborators.remove(userService.readById(userId));
        todo.setCollaborators(collaborators);
        todoService.update(todo);
        return "redirect:/todos/" + id + "/tasks";
    }

    public boolean canReadToDo(long todoId) {
        WebAuthToken authentication
                = (WebAuthToken) SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getDetails();
        ToDo todo = todoService.readById(todoId);
        boolean isCollaborator = todo.getCollaborators().stream().anyMatch((collaborator)
                -> collaborator.getId().equals(user.getId()));
        return user.getId().equals(todo.getOwner().getId()) || isCollaborator;
    }
}
