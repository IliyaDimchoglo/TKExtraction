package com.tkextraction.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionMessageDto {
    private String title;
    private String message;
    private String key;
    private String description;
}
