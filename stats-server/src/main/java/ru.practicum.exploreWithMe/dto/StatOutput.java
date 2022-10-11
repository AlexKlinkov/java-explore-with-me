package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatOutput {
    private String app; // name of service where was made a request (in our case 'ewn')
    private String uri; // defined path to resource
    private Long hits; // quantity of apply this request by app and uri
}
