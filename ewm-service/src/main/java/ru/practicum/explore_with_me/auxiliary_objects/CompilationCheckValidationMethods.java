package ru.practicum.explore_with_me.auxiliary_objects;

import ru.practicum.explore_with_me.exeption.NotCorrectArgumentsInMethodException;
import ru.practicum.explore_with_me.exeption.ValidationException;

public class CompilationCheckValidationMethods {
    public static void checkParamsOfPageFromAndSize(Long from, Long size) throws NotCorrectArgumentsInMethodException {
        if (size < 1 || from < 0) {
            throw new NotCorrectArgumentsInMethodException("Size cannot be less than 1, also " +
                    "from cannot be less then 0");
        }
    }
    public static void checkParamOfId(Long id, String nameId) throws ValidationException {
        if (id < 0) {
            throw new ValidationException("Id cannot be less than 0, yours is - " + nameId + " = " + id);
        }
    }
}
