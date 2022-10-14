package ru.practicum.explore_with_me.exeption;

public class ServerError extends RuntimeException {
    public ServerError(String message) {
        super(message);
    }
}
