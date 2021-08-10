package com.tkextraction.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final String title;

    public BaseException(String title, String message) {
        super(message);
        this.title = title;
    }
}