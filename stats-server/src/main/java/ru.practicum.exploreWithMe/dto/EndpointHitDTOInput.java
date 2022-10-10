package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDTOInput {
    private String app; // name of service where was made a request (in our case 'ewn')
    private String uri; // ip address (localhost) + defined path to resource
    private String ip; // ip address of user who made a request
    private LocalDateTime timestamp; // time when this request was made by user
}
