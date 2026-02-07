package com.legalcms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourtDataResponse {

    private String cnrNumber;
    private String caseNumber;
    private String courtName;
}
