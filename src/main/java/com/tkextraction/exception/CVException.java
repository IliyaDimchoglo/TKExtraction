package com.tkextraction.exception;

public class CVException extends BaseException{

    private static final String TITLE = "CV error.";

    public CVException(String title, String message) {
        super(title, message);
    }
    public static CVException cvNotFound(){
        return new CVException(TITLE, "CV not found.");
    }

    public static CVException formatNotSupported(String currentFormat, String availableFormat){
        return new CVException(TITLE, String.format("Format: %s, not supported, available format: %s", currentFormat, availableFormat));
    }
}
