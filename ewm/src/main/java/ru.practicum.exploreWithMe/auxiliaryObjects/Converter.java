package ru.practicum.exploreWithMe.auxiliaryObjects;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Converter {

    public LocalDateTime ConverterStringToLocalDataTime (String date) {
        String[] dataAndTimeArray = date.split(" ");
        String[] onlyDateArray = dataAndTimeArray[0].split("-");
        String[] onlyTimeArray = dataAndTimeArray[1].split(":");
        return LocalDateTime.of(
                Integer.parseInt(onlyDateArray[0]), Integer.parseInt(onlyDateArray[1]), Integer.parseInt(onlyDateArray[2]),
                Integer.parseInt(onlyTimeArray[0]), Integer.parseInt(onlyTimeArray[1]),
                Integer.parseInt(onlyTimeArray[2])
        );

    }
}
