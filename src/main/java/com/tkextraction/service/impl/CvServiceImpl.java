package com.tkextraction.service.impl;

import com.tkextraction.domain.CVEntity;
import com.tkextraction.domain.enums.Status;
import com.tkextraction.dto.Resume;
import com.tkextraction.dto.RetrieveResponse;
import com.tkextraction.exception.CVException;
import com.tkextraction.parser.ResumeParserService;
import com.tkextraction.repository.CVRepository;
import com.tkextraction.service.CvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.tkextraction.utils.TransactionRandomizer.getRandomIdTransaction;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class CvServiceImpl implements CvService {

    private final CVRepository cvRepository;
    private final ResumeParserService resumeParserService;

    private static final String FORMAT = "pdf";

    @Override
    public Long submit(String userName, MultipartFile file) {
        if (requireNonNull(file.getContentType()).contains(FORMAT)) {
            CVEntity cv = cvRepository.findByUserUserName(userName)
                    .orElseThrow(CVException::cvNotFound);
            final Long processId = getRandomIdTransaction();
            cv.setProcessId(processId);
            cv.runAsyncUpdateCVSourceAndStatus(file);
            log.info("[x] Process id: {}", processId);
            return processId;
        } else {
            log.error("[x] Format not supported. ContentType: {}, user name: {}", file.getContentType(), userName);
            throw CVException.formatNotSupported(file.getContentType(), FORMAT);
        }
    }

    @Override
    public RetrieveResponse retrieve(String userName, Long processId) {
        final CVEntity cvEntity = cvRepository.findByProcessIdAndUserUserName(processId, userName)
                .orElseThrow(CVException::cvNotFound);
        if (cvEntity.getProcessStatus().equals(Status.PROGRESS)) {
            log.info("[x] CV in status 'PROGRESS', userName: {} ", userName);
            return RetrieveResponse.builder().status(cvEntity.getProcessStatus()).build();
        } else {
            final Resume parsedResume = resumeParserService.parse(cvEntity.getCvSource());
            return RetrieveResponse.builder()
                    .status(cvEntity.getProcessStatus())
                    .resume(parsedResume)
                    .build();
        }
    }
}
