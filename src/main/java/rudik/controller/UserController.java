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
import rudik.exception.NullEntityReferenceException;
import rudik.model.User;
import rudik.service.RoleService;
import rudik.service.UserService;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RoleService roleService;


    @GetMapping("/create")
    @ApiOperation("Create user")
    @PreAuthorize("hasAuthority('ADMIN') or isAnonymous()")
    public String create(Model model) {
        logger.info("Create user (GET)");
        model.addAttribute("user", new User());
        return "create-user";
    }

    @PostMapping("/create")
    @ApiOperation("Create valid user")
    @PreAuthorize("hasAuthority('ADMIN') or isAnonymous()")
    public String create(@Validated @ModelAttribute("user") User user, BindingResult result) throws EntityNotFoundException, NullEntityReferenceException, NullEntityReferenceException {
        if (result.hasErrors()) {
            logger.warn("Create invalid user (POST)");
            return "create-user";
        }
        user.setPassword(user.getPassword());
        user.setRole(roleService.readById(2));
        User newUser = userService.create(user);
        logger.info("Create valid user (POST)");
        return "redirect:/todos/all/users/" + newUser.getId();
    }

    @GetMapping("/{id}/read")
    @ApiOperation("Read user info by id")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.details.id == #id")
    public String read(@PathVariable long id, Model model) throws EntityNotFoundException {
        logger.info("Read user by id " + id);
        User user = userService.readById(id);
        model.addAttribute("user", user);
        return "user-info";
    }

    @GetMapping("/{id}/update")
    @ApiOperation("Update user by id")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.details.id == #id")
    public String update(@PathVariable long id, Model model) throws EntityNotFoundException {
        logger.info("Update user by id " + id + " (GET)");
        User user = userService.readById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAll());
        return "update-user";
    }


    @PostMapping("/{id}/update")
    @ApiOperation("Update valid user by id")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.details.id == #id")
    public String update(@PathVariable long id, Model model, @Validated @ModelAttribute("user") User user, @RequestParam("roleId") long roleId, BindingResult result) throws EntityNotFoundException, NullEntityReferenceException {
        User oldUser = userService.readById(id);
        if (result.hasErrors()) {
            user.setRole(oldUser.getRole());
            model.addAttribute("roles", roleService.getAll());
            logger.warn("Update invalid user (POST)");
            return "update-user";
        }
        if (oldUser.getRole().getName().equals("USER")) {
            user.setRole(oldUser.getRole());
        } else {
            user.setRole(roleService.readById(roleId));
        }
        userService.update(user);
        logger.info("Update valid user (POST)");
        return "redirect:/users/" + id + "/read";
    }


    @DeleteMapping("/{id}/delete")
    @ApiOperation("Delete user by id")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and authentication.details.id == #id")
    public String delete(@PathVariable("id") long id) throws EntityNotFoundException {
        logger.info("Delete user with id " + id);
        userService.delete(id);
        return "redirect:/users/all";
    }

    @GetMapping("/all")
    @ApiOperation("Get all users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAll(Model model) {
        logger.info("Get all users");
        model.addAttribute("users", userService.getAll());
        return "users-list";
    }
}
