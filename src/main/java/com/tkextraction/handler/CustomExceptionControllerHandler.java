package com.tkextraction.handler;

import com.tkextraction.exception.BaseException;
import com.tkextraction.dto.ExceptionMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionControllerHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionMessageDto> defaultExceptionHandler(final Throwable ex) {
        log.error(ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(ExceptionMessageDto.builder()
                .title("Something went wrong")
                .message("Please, try again later.")
                .description(ex.getLocalizedMessage())
                .build());
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionMessageDto> clientExceptionHandler(final BaseException exception) {
        log.error(exception.getLocalizedMessage());
        return ResponseEntity.status(432).body(ExceptionMessageDto.builder()
                .title(exception.getTitle())
                .message(exception.getLocalizedMessage())
                .description(exception.getLocalizedMessage())
                .build());
    }
}