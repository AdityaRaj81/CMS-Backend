package com.legalcms.controller;

import com.legalcms.dto.CaseStatusResponse;
import com.legalcms.dto.CourtDataRequest;
import com.legalcms.dto.CourtDataResponse;
import com.legalcms.service.CourtIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/court")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Court Integration", description = "Endpoints for integrating with court systems (Patna HC, Barh Civil Court)")
public class CourtIntegrationController {

    private final CourtIntegrationService courtIntegrationService;

    @PostMapping("/fetch-cnr")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADVOCATE')")
    @Operation(
        summary = "Fetch CNR number", 
        description = "Fetch CNR number from court system using case number and year. " +
                      "Designed for future integration with LegalKart/Attestr or custom scraper."
    )
    public ResponseEntity<CourtDataResponse> fetchCnrNumber(@Valid @RequestBody CourtDataRequest request) {
        CourtDataResponse response = courtIntegrationService.fetchCnrNumber(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/case-status/{cnrNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADVOCATE')")
    @Operation(
        summary = "Get case status by CNR", 
        description = "Fetch latest case status including next hearing date, stage, and last order URL. " +
                      "Returns data from court portal via third-party API or scraper."
    )
    public ResponseEntity<CaseStatusResponse> getCaseStatus(@PathVariable String cnrNumber) {
        CaseStatusResponse response = courtIntegrationService.fetchCaseStatus(cnrNumber);
        return ResponseEntity.ok(response);
    }
}
