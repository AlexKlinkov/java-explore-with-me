package ru.practicum.exploreWithMe.exeption;

public class ServerError extends RuntimeException {
    private String reason;

    public ServerError(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public ServerError(String message) {
        super(message);
    }
}
