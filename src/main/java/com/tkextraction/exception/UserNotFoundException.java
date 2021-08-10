package com.tkextraction.exception;

public class UserNotFoundException extends BaseException {

    private static final String TITLE = "User error.";

    public UserNotFoundException(String title, String message) {
        super(title, message);
    }

    public static UserNotFoundException userNotFound() {
        return new UserNotFoundException(TITLE, "User not found.");
    }
}
