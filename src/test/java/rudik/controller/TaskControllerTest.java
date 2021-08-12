package rudik.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rudik.dto.TaskDTO;
import rudik.dto.TaskTransformer;
import rudik.model.Priority;
import rudik.model.State;
import rudik.model.Task;
import rudik.model.ToDo;
import rudik.service.StateService;
import rudik.service.TaskService;
import rudik.service.ToDoService;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ToDoService toDoService;

    @MockBean
    private StateService stateService;

    @Test
    void shouldGetCreate() throws Exception{
        Mockito.doReturn(new ToDo())
                .when(toDoService)
                .readById(5);

        ToDo expected = toDoService.readById(5);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET,"/tasks/create/todos/5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("task"))
                .andExpect(MockMvcResultMatchers.model().attribute("todo",expected))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities", Priority.values()));
    }

    @Test
    void shouldPostCreate() throws Exception{
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Victor");
        taskDTO.setPriority("LOW");
        taskDTO.setTodoId(5);

        Mockito.doReturn(new ToDo())
                .when(toDoService)
                .readById(taskDTO.getTodoId());

        Mockito.doReturn(new State())
                .when(stateService)
                .getByName("Victor");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST,"/tasks/create/todos/5")
                .param("name",taskDTO.getName())
                .param("priority",taskDTO.getPriority())
                .param("todoId",String.valueOf(taskDTO.getTodoId())))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/5/tasks"));
    }

    @Test
    void shouldPostCreateWithBingingResultErrors() throws Exception {
        TaskDTO taskDto = new TaskDTO();
        taskDto.setName("");
        taskDto.setPriority("LOW");
        taskDto.setTodoId(5);

        ToDo toDo = new ToDo();

        Mockito.doReturn(toDo)
                .when(toDoService)
                .readById(taskDto.getTodoId());

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/tasks/create/todos/5")
                .param("name", taskDto.getName())
                .param("priority", taskDto.getPriority())
                .param("todoId", String.valueOf(taskDto.getTodoId())))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.model().attribute("todo", toDo))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities", Priority.values()))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void getUpdate() throws Exception {

        List<State> states = Arrays.asList(new State(), new State(), new State());

        Task task = new Task();
        task.setId(1);
        task.setName("name");
        task.setPriority(Priority.LOW);
        task.setTodo(new ToDo());
        task.setState(new State());

        Mockito.doReturn(task)
                .when(taskService)
                .readById(1);
        Mockito.doReturn(states)
                .when(stateService)
                .getAll();

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/tasks/1/update/todos/5"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("task"))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities", Priority.values()))
                .andExpect(MockMvcResultMatchers.model().attribute("states", states))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }

    @Test
    void postUpdate() throws Exception {

        Task task = new Task();
        task.setId(1);
        task.setName("Name");
        task.setPriority(Priority.LOW);
        task.setTodo(new ToDo());
        task.setState(new State());

        task.getTodo().setId(5);

        TaskDTO taskDto = TaskTransformer.convertToDTO(task);

        Mockito.doReturn(task.getTodo())
                .when(toDoService)
                .readById(5);
        Mockito.doReturn(new State())
                .when(stateService)
                .readById(0);
        Mockito.doReturn(task)
                .when(taskService)
                .update(task);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/tasks/1/update/todos/5")
                .param("id", String.valueOf(taskDto.getId()))
                .param("name", taskDto.getName())
                .param("priority", taskDto.getPriority())
                .param("todoId", String.valueOf(taskDto.getTodoId()))
                .param("stateId", String.valueOf(taskDto.getStateId())))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/5/tasks"));

    }

    @Test
    void postUpdateWithBindingResultErrors() throws Exception {

        List<State> states = Arrays.asList(new State(), new State(), new State());

        Task task = new Task();
        task.setId(1);
        task.setName("");
        task.setPriority(Priority.LOW);
        task.setTodo(new ToDo());
        task.setState(new State());

        task.getTodo().setId(5);

        TaskDTO taskDto = TaskTransformer.convertToDTO(task);

        Mockito.doReturn(states)
                .when(stateService)
                .getAll();


        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/tasks/1/update/todos/5")
                .param("id", String.valueOf(taskDto.getId()))
                .param("name", taskDto.getName())
                .param("priority", taskDto.getPriority())
                .param("todoId", String.valueOf(taskDto.getTodoId()))
                .param("stateId", String.valueOf(taskDto.getStateId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.model().attribute("priorities", Priority.values()))
                .andExpect(MockMvcResultMatchers.model().attribute("states", states));
    }

    @Test
    void shouldDelete() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/tasks/1/delete/todos/5"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/5/tasks"));

    }

}
