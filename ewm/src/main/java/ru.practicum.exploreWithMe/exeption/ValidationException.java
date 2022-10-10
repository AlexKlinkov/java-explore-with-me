package ru.practicum.exploreWithMe.exeption;

public class ValidationException extends RuntimeException{
    private String reason;

    public ValidationException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public ValidationException(String message) {
        super(message);
    }
}
