package ru.practicum.explore_with_me.auxiliary_objects;

public class CompilationCheckValidationMethods {
    public static boolean checkParamsOfPageFromAndSize(Long from, Long size) {
        if (size < 1 || from < 0) {
            return true;
        }
        return false;
    }
    public static boolean checkParamOfId(Long id){
        if (id < 0) {
            return true;
        }
        return false;
    }
}
