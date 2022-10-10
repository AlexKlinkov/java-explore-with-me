package ru.practicum.exploreWithMe.exeption;

public class NotFoundException extends RuntimeException {
    private String reason;

    public NotFoundException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
    public NotFoundException(String message) {
        super(message);
    }
}
