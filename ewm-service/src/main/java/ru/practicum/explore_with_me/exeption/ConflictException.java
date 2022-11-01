package ru.practicum.explore_with_me.exeption;

public class ConflictException extends RuntimeException{
    public ConflictException(String message) {
        super(message);
    }
}
