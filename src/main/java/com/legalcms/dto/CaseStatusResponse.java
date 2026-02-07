package com.legalcms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseStatusResponse {

    private String cnrNumber;
    private LocalDate nextHearingDate;
    private String caseStage;
    private String lastOrderUrl;
}
