package dto;

import model.Task;

public class TaskTransformer {

    public static TaskDTO convertToDo(Task task){
        return new TaskDTO(
                task.getId(),
                task.getName(),
                task.getPriority().toString(),
                task.getToDo().getId(),
                task.getState().getId()
                );
    }
}
