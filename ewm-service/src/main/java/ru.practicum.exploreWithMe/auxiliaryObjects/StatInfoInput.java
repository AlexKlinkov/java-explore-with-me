package ru.practicum.exploreWithMe.auxiliaryObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatInfoInput {
    private String app; // name of service where was made a request (in our case 'ewn')
    private String uri; // defined path to resource
    private String ip; // ip address of user who made a request
    private String timestamp; // data with time when this request was made by user
}
