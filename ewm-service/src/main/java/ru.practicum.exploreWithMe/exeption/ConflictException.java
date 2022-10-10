package ru.practicum.exploreWithMe.exeption;

public class ConflictException extends RuntimeException{
    private String reason;

    public ConflictException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public ConflictException(String message) {
        super(message);
    }
}
