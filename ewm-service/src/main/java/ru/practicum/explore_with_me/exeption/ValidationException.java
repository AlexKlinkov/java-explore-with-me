package ru.practicum.explore_with_me.exeption;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
