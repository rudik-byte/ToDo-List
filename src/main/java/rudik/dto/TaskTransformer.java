package rudik.dto;

import rudik.model.Priority;
import rudik.model.State;
import rudik.model.Task;
import rudik.model.ToDo;

public class TaskTransformer {

    public static TaskDTO convertToDTO(Task task){
        return new TaskDTO(
                task.getId(),
                task.getName(),
                task.getPriority().toString(),
                task.getToDo().getId(),
                task.getState().getId()
                );
    }
    public static Task convertToEntity(TaskDTO taskDto, ToDo todo, State state) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        task.setToDo(todo);
        task.setState(state);
        return task;
    }
}
