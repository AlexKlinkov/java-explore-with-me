package ru.practicum.explore_with_me.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestControllerAdvice
public class ErrorHandlerException {

    // 400 — если ошибка валидации: ValidationException
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNotCorrectValidate(ValidationException exception) {
        return new Error(new ArrayList<>(), HttpStatus.BAD_REQUEST,
                "For the requested operation the conditions are not met.", exception.getMessage(),
                LocalDateTime.now());
    }

    // 403
    @ExceptionHandler(NotCorrectArgumentsInMethodException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Error handleNotCorrectArgumentsInMethod(NotCorrectArgumentsInMethodException exception) {
        return new Error(new ArrayList<>(), HttpStatus.FORBIDDEN,
                "For the requested operation the conditions are not met.", exception.getMessage(),
                LocalDateTime.now());
    }

    // 404 — для всех ситуаций, если искомый объект не найден
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(NotFoundException exception) {
        return new Error(HttpStatus.NOT_FOUND, "The required object was not found.", exception.getMessage(),
                LocalDateTime.now());
    }

    // 409 — нарушение целостности данных
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleConflictException(ConflictException exception) {
        return new Error(HttpStatus.CONFLICT, "Integrity constraint has been violated", exception.getMessage(),
                LocalDateTime.now());
    }

    // 500 — если возникло исключение
    @ExceptionHandler(ServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleServerError(ServerError exception) {
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred", exception.getMessage(),
                LocalDateTime.now());
    }
}
