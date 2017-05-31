package ua.nure.serhieiev.library.controller.util;

import java.util.Arrays;

public final class Validator {

    public static boolean isInteger(String... params){
        for(String param : params){
            try {
                Integer.parseInt(param);
            } catch(NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public static boolean isString(String... params){
        return Arrays.stream(params).noneMatch(param -> param == null || param.trim().isEmpty());
    }

    private Validator() {
    }

}
