package com.legalcms.dto;

import com.legalcms.model.CaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseResponse {

    private Long id;
    private String caseTitle;
    private String caseType;
    private String caseNumber;
    private String cnrNumber;
    private String courtName;
    private LocalDate nextHearingDate;
    private String caseStage;
    private CaseStatus status;
    private UserResponse assignedAdvocate;
    private List<PartyResponse> parties;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
