package com.legalcms.service;

import com.legalcms.dto.HearingResponse;
import com.legalcms.model.CaseEntity;
import com.legalcms.model.Hearing;
import com.legalcms.repository.CaseRepository;
import com.legalcms.repository.HearingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HearingService {

    private final HearingRepository hearingRepository;
    private final CaseRepository caseRepository;

    @Transactional(readOnly = true)
    public List<HearingResponse> getHearingsByCaseId(Long caseId) {
        log.info("Fetching hearings for case ID: {}", caseId);
        CaseEntity caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        List<Hearing> hearings = hearingRepository.findByCaseEntityOrderByHearingDateDesc(caseEntity);
        return hearings.stream()
                .map(this::mapToHearingResponse)
                .collect(Collectors.toList());
    }

    private HearingResponse mapToHearingResponse(Hearing hearing) {
        return HearingResponse.builder()
                .id(hearing.getId())
                .caseId(hearing.getCaseEntity().getId())
                .hearingDate(hearing.getHearingDate())
                .stage(hearing.getStage())
                .remarks(hearing.getRemarks())
                .build();
    }
}
