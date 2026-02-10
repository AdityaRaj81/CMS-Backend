package com.legalcms.service;

import com.legalcms.dto.CaseRequest;
import com.legalcms.dto.CaseResponse;
import com.legalcms.dto.PartyResponse;
import com.legalcms.dto.UserResponse;
import com.legalcms.model.*;
import com.legalcms.repository.CaseRepository;
import com.legalcms.repository.PartyRepository;
import com.legalcms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaseService {

    private final CaseRepository caseRepository;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

    @Transactional
    public CaseResponse createCase(CaseRequest request) {
        log.info("Creating new case: {}", request.getCaseNumber());

        if (caseRepository.findByCaseNumber(request.getCaseNumber()).isPresent()) {
            throw new RuntimeException("Case number already exists");
        }

        User assignedAdvocate = null;
        if (request.getAssignedAdvocateId() != null) {
            assignedAdvocate = userRepository.findById(request.getAssignedAdvocateId())
                    .orElseThrow(() -> new RuntimeException("Advocate not found"));

            if (assignedAdvocate.getRole() != UserRole.ADVOCATE) {
                throw new RuntimeException("Assigned user is not an advocate");
            }
        }

        CaseEntity caseEntity = CaseEntity.builder()
                .caseTitle(request.getCaseTitle())
                .caseType(request.getCaseType())
                .caseNumber(request.getCaseNumber())
                .cnrNumber(request.getCnrNumber())
                .courtName(request.getCourtName())
                .nextHearingDate(request.getNextHearingDate())
                .caseStage(request.getCaseStage())
                .status(request.getStatus())
                .assignedAdvocate(assignedAdvocate)
                .build();

        caseEntity = caseRepository.save(caseEntity);

        // Add parties if provided
        if (request.getPetitionerName() != null && request.getRespondentName() != null) {
            Party party = Party.builder()
                    .caseEntity(caseEntity)
                    .petitionerName(request.getPetitionerName())
                    .respondentName(request.getRespondentName())
                    .build();
            partyRepository.save(party);
        }

        log.info("Case created successfully with ID: {}", caseEntity.getId());
        return mapToCaseResponse(caseEntity);
    }

    @Transactional(readOnly = true)
    public List<CaseResponse> getAllCases() {
        log.info("Fetching all cases");
        List<CaseEntity> cases = caseRepository.findAll();
        return cases.stream()
                .map(this::mapToCaseResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CaseResponse> getMyCases(String email) {
        log.info("Fetching cases for advocate: {}", email);
        User advocate = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CaseEntity> cases = caseRepository.findByAssignedAdvocate(advocate);
        return cases.stream()
                .map(this::mapToCaseResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CaseResponse getCaseById(Long caseId) {
        log.info("Fetching case by ID: {}", caseId);
        CaseEntity caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + caseId));
        return mapToCaseResponse(caseEntity);
    }

    @Transactional(readOnly = true)
    public List<CaseResponse> searchCases(String caseNumber, String cnrNumber, String partyName) {
        log.info("Searching cases with params - caseNumber: {}, cnrNumber: {}, partyName: {}",
                caseNumber, cnrNumber, partyName);

        List<CaseEntity> cases;

        if (partyName != null && !partyName.isEmpty()) {
            cases = caseRepository.findByPartyName(partyName);
        } else {
            cases = caseRepository.searchCases(caseNumber, cnrNumber, null);
        }

        return cases.stream()
                .map(this::mapToCaseResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CaseEntity> getActiveCasesWithCnr() {
        return caseRepository.findByStatusAndCnrNumberIsNotNull(CaseStatus.ACTIVE);
    }

    @Transactional
    public CaseResponse updateCase(Long caseId, CaseRequest request) {
        log.info("Updating case ID: {}", caseId);
        CaseEntity caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        caseEntity.setCaseTitle(request.getCaseTitle());
        caseEntity.setCaseType(request.getCaseType());
        caseEntity.setCaseNumber(request.getCaseNumber());
        caseEntity.setCnrNumber(request.getCnrNumber());
        caseEntity.setCourtName(request.getCourtName());
        caseEntity.setNextHearingDate(request.getNextHearingDate());
        caseEntity.setCaseStage(request.getCaseStage());
        caseEntity.setStatus(request.getStatus());

        caseEntity = caseRepository.save(caseEntity);
        log.info("Case updated successfully with ID: {}", caseId);
        return mapToCaseResponse(caseEntity);
    }

    @Transactional
    public void deleteCase(Long caseId) {
        log.info("Deleting case ID: {}", caseId);
        if (!caseRepository.existsById(caseId)) {
            throw new RuntimeException("Case not found");
        }
        caseRepository.deleteById(caseId);
        log.info("Case deleted successfully with ID: {}", caseId);
    }

    @Transactional
    public void updateCaseDetails(Long caseId, String caseStage, java.time.LocalDate nextHearingDate) {
        log.info("Updating case ID: {} with stage: {} and hearing date: {}", caseId, caseStage, nextHearingDate);
        CaseEntity caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        caseEntity.setCaseStage(caseStage);
        caseEntity.setNextHearingDate(nextHearingDate);
        caseRepository.save(caseEntity);
    }

    private CaseResponse mapToCaseResponse(CaseEntity caseEntity) {
        UserResponse advocateResponse = null;
        if (caseEntity.getAssignedAdvocate() != null) {
            User advocate = caseEntity.getAssignedAdvocate();
            advocateResponse = UserResponse.builder()
                    .id(advocate.getId())
                    .fullName(advocate.getFullName())
                    .email(advocate.getEmail())
                    .role(advocate.getRole())
                    .build();
        }

        List<PartyResponse> partyResponses = caseEntity.getParties().stream()
                .map(party -> PartyResponse.builder()
                        .id(party.getId())
                        .petitionerName(party.getPetitionerName())
                        .respondentName(party.getRespondentName())
                        .build())
                .collect(Collectors.toList());

        return CaseResponse.builder()
                .id(caseEntity.getId())
                .caseTitle(caseEntity.getCaseTitle())
                .caseType(caseEntity.getCaseType())
                .caseNumber(caseEntity.getCaseNumber())
                .cnrNumber(caseEntity.getCnrNumber())
                .courtName(caseEntity.getCourtName())
                .nextHearingDate(caseEntity.getNextHearingDate())
                .caseStage(caseEntity.getCaseStage())
                .status(caseEntity.getStatus())
                .assignedAdvocate(advocateResponse)
                .parties(partyResponses)
                .createdAt(caseEntity.getCreatedAt())
                .updatedAt(caseEntity.getUpdatedAt())
                .build();
    }
}
