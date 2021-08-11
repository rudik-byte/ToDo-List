package rudik.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    @NotNull
    private long id;

    @NotBlank(message = "The 'name' cannot be empty")
    private String name;

    @NotBlank
    private String priority;

    @NotNull
    private long todoId;

    @NotNull
    private long stateId;
}
