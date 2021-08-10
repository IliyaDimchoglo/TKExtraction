package com.tkextraction.controller;

import com.tkextraction.dto.ProcessDto;
import com.tkextraction.dto.RetrieveResponse;
import com.tkextraction.service.CvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Controller
@RequestMapping(value = "/cvs")
@RequiredArgsConstructor
public class ExtractionController {

    private final CvService cvService;

    @PostMapping(value = "/submit")
    public ResponseEntity<ProcessDto> submit(@RequestParam(name = "file") @NotNull MultipartFile file, Principal principal) {
        return ResponseEntity.ok(ProcessDto.of(cvService.submit(principal.getName(), file)));
    }

    @GetMapping(value = "/retrieve", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public ResponseEntity<RetrieveResponse> retrieve(@RequestParam(name = "processId") Long processId, Principal principal) {
       return ResponseEntity.ok(cvService.retrieve(principal.getName(), processId));
    }
}
