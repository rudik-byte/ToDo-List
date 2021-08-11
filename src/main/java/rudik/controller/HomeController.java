package rudik.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rudik.service.UserService;

@RestController
@Controller
@RequestMapping("/home")
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiOperation("Get all users")
    public String home(Model model) {
        model.addAttribute("users", userService.getAll());
        logger.info("Get all users");
        return "home";
    }
}
