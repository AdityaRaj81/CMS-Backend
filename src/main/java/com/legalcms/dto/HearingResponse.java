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
public class HearingResponse {

    private Long id;
    private Long caseId;
    private LocalDate hearingDate;
    private String stage;
    private String remarks;
}
