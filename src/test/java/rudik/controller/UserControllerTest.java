package rudik.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rudik.model.User;
import rudik.service.RoleService;
import rudik.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateUserGetTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", new User()));
    }

    @Test
    public void shouldCreateUserPostTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/create")
                .param("firstName", "Victor")
                .param("lastName", "Pavlik")
                .param("email", "vicpavlik@gmail.com")
                .param("password", "qwertY12345"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void shouldReadUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/4/read"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", userService.readById(4)));
    }

    @Test
    public void shouldUpdateUserGetTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/4/read"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("roles"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", userService.readById(4)))
                .andExpect(MockMvcResultMatchers.model().attribute("roles", roleService.getAll()));
    }

    @Test
    public void shouldUpdateUserPostTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/4/update")
                .param("id", "4")
                .param("firstName", "Victor")
                .param("lastName", "Pavlik")
                .param("email", "vicpavlik@gmail.com")
                .param("oldPassword", "qwertY12345")
                .param("password", "qwertY12345")
                .param("roleId", "2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void shouldDeleteUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/4/delete"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void shouldGetAllUsersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", userService.getAll()));
    }
}
