package com.tkextraction.service;

import com.tkextraction.dto.RetrieveResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface CvService {

    @Transactional
    Long submit(String userName, MultipartFile file);

    @Transactional(readOnly = true)
    RetrieveResponse retrieve(String userName, Long processId);
}
