package com.tkextraction.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.tkextraction.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveResponse {
    private Status status;
    private Resume resume;
}
