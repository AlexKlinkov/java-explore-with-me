package ru.practicum.exploreWithMe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    private String app; // name of service where was made a request (in our case 'ewn')
    private String uri; // ip address (localhost) + defined path to resource
    private Long hits; // quantity of views
}
