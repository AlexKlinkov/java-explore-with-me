package ru.practicum.exploreWithMe.exeption;

public class NotCorrectArgumentsInMethodException extends RuntimeException{
    private String reason;

    public NotCorrectArgumentsInMethodException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
    public NotCorrectArgumentsInMethodException(String message) {
        super(message);
    }
}
