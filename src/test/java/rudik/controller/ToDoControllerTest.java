package rudik.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import rudik.configartion.AbstractTestContainers;
import rudik.model.ToDo;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ToDoControllerTest extends AbstractTestContainers {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateGetTest() throws Exception {
        Long ownerId = 6L;
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/create/users/" + ownerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo"))
                .andExpect(MockMvcResultMatchers.model().attribute("ownerId", ownerId))
                .andExpect(MockMvcResultMatchers.view().name("create-todo"));

    }

    @Test
    public void shouldReadGetTest() throws Exception {
        long id = 7L;
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + id + "/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo", "tasks", "users"))
                .andExpect(MockMvcResultMatchers.view().name("todo-tasks"));
    }

    @Test
    public void shouldUpdateGetTest() throws Exception {
        long ownerId = 6L;
        long toDoId = 7L;

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + toDoId + "/update/users/" + ownerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo"))
                .andExpect(MockMvcResultMatchers.view().name("update-todo"));
    }

    @Test
    public void shouldUpdatePostTest() throws Exception {
        long ownerId = 4L;
        long toDoId = 7L;

        ToDo toDo = new ToDo();
        toDo.setId(toDoId);
        toDo.setTitle("title");
        toDo.setCreatedAt(LocalDateTime.now());
        mockMvc.perform(MockMvcRequestBuilders.post("/todos/" + toDoId + "/update/users/" + ownerId)
                .flashAttr("todo", toDo))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/todos/all/users/" + ownerId));
    }

    @Test
    public void shouldUpdatePostTestException() throws Exception {
        long ownerId = 4L;
        long toDoId = 7L;

        ToDo toDo = new ToDo();
        toDo.setId(toDoId);
        toDo.setTitle("   ");
        toDo.setCreatedAt(LocalDateTime.now());
        mockMvc.perform(MockMvcRequestBuilders.post("/todos/" + toDoId + "/update/users/" + ownerId)
                .flashAttr("todo", toDo))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update-todo"));
    }

    @Test
    public void shouldGetAllTest() throws Exception {
        long idUser = 6L;
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/all/users/" + idUser))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todos", "user"))
                .andExpect(MockMvcResultMatchers.view().name("todos-user"));
    }

    @Test
    public void shouldDeleteGetTest() throws Exception {
        long todoId = 13L;
        long ownerId = 6L;

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + todoId + "/delete/users/" + ownerId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/todos/all/users/" + ownerId));
    }

    @Test
    public void shouldAddCollaborator() throws Exception {
        long id = 12L;
        long userId = 6L;
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + id + "/add")
                .param("user_id", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/todos/" + id + "/tasks"));
    }

    @Test
    public void shouldRemoveCollaborator() throws Exception {
        long id = 7L;
        long userId = 6L;

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + id + "/remove").param("user_id", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/todos/" + id + "/tasks"));

    }
}
