package com.tkextraction.repository;

import com.tkextraction.domain.CVEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CVRepository extends JpaRepository<CVEntity, UUID> {

    Optional<CVEntity> findByProcessIdAndUserUserName(Long processId, String userName);

    Optional<CVEntity> findByUserUserName(String userName);

}
