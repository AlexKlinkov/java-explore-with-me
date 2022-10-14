package explore_with_me.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDTOInput {
    @NotEmpty
    private String app; // name of service where was made a request (in our case 'ewn')
    @NotEmpty
    private String uri; // defined path to resource
    @NotEmpty
    private String ip; // ip address of user who made a request
    @NotNull
    private String timestamp; // time when this request was made by user
}
