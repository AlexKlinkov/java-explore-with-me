package ru.practicum.explore_with_me.exeption;

public class NotCorrectArgumentsInMethodException extends RuntimeException{
    public NotCorrectArgumentsInMethodException(String message) {
        super(message);
    }
}
