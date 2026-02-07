package com.legalcms.dto;

import com.legalcms.model.CaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseRequest {

    @NotBlank(message = "Case title is required")
    private String caseTitle;

    @NotBlank(message = "Case type is required")
    private String caseType;

    @NotBlank(message = "Case number is required")
    private String caseNumber;

    private String cnrNumber;

    @NotBlank(message = "Court name is required")
    private String courtName;

    private LocalDate nextHearingDate;

    private String caseStage;

    @NotNull(message = "Case status is required")
    @Builder.Default
    private CaseStatus status = CaseStatus.ACTIVE;

    private Long assignedAdvocateId;

    private String petitionerName;
    private String respondentName;
}
