package com.legalcms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourtDataRequest {

    @NotBlank(message = "Case number is required")
    private String caseNumber;

    @NotBlank(message = "Year is required")
    private String year;

    @NotBlank(message = "Court name is required")
    private String courtName;
}
