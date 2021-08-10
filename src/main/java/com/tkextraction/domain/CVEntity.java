package com.tkextraction.domain;


import com.tkextraction.domain.enums.Status;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Slf4j
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cv")
@EqualsAndHashCode(callSuper = false)
public class CVEntity extends BaseUpdatedEntity {

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private Status processStatus;

    @Column(unique = true)
    private Long processId;

    private String cvDocumentType;

    private String cvName;

    @Lob
    private byte[] cvSource;

    public CVEntity(UserEntity user) {
        this.user = user;
    }

    @Async
    @Transactional
    @SneakyThrows
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void runAsyncUpdateCVSourceAndStatus(MultipartFile file){
        log.info("[x] Asynchronous cv processing has started.");
        processStatus = Status.COMPLETED;
        cvSource = file.getBytes();
        cvName = file.getOriginalFilename();
        cvDocumentType = file.getContentType();
    }
}
