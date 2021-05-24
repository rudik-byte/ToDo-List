package rudik.dto;

import rudik.model.Task;

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
}
