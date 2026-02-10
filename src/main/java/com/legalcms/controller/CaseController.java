package com.legalcms.controller;

import com.legalcms.dto.CaseRequest;
import com.legalcms.dto.CaseResponse;
import com.legalcms.service.CaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Case Management", description = "Endpoints for managing legal cases")
public class CaseController {

    private final CaseService caseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADVOCATE')")
    @Operation(summary = "Create new case", description = "Create a new legal case with manual entry")
    public ResponseEntity<CaseResponse> createCase(@Valid @RequestBody CaseRequest request) {
        CaseResponse response = caseService.createCase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADVOCATE')")
    @Operation(summary = "Create new case", description = "Create a new legal case with manual entry (deprecated, use POST /api/cases)")
    public ResponseEntity<CaseResponse> createCaseDeprecated(@Valid @RequestBody CaseRequest request) {
        CaseResponse response = caseService.createCase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all cases", description = "Returns all legal cases with pagination")
    public ResponseEntity<List<CaseResponse>> getAllCases() {
        List<CaseResponse> cases = caseService.getAllCases();
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ADVOCATE')")
    @Operation(summary = "Get my cases", description = "Returns cases assigned to logged-in advocate")
    public ResponseEntity<List<CaseResponse>> getMyCases(Authentication authentication) {
        String email = authentication.getName();
        List<CaseResponse> cases = caseService.getMyCases(email);
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/{caseId}")
    @Operation(summary = "Get case by ID", description = "Returns full case details by ID (for Case Detail Page)")
    public ResponseEntity<CaseResponse> getCaseById(@PathVariable Long caseId) {
        CaseResponse response = caseService.getCaseById(caseId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{caseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADVOCATE')")
    @Operation(summary = "Update case", description = "Update an existing legal case")
    public ResponseEntity<CaseResponse> updateCase(@PathVariable Long caseId, @Valid @RequestBody CaseRequest request) {
        CaseResponse response = caseService.updateCase(caseId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{caseId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete case", description = "Delete a legal case")
    public ResponseEntity<Void> deleteCase(@PathVariable Long caseId) {
        caseService.deleteCase(caseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search cases", description = "Search cases by case number, CNR number, or party name")
    public ResponseEntity<List<CaseResponse>> searchCases(
            @RequestParam(required = false) String caseNumber,
            @RequestParam(required = false) String cnrNumber,
            @RequestParam(required = false) String partyName) {
        List<CaseResponse> cases = caseService.searchCases(caseNumber, cnrNumber, partyName);
        return ResponseEntity.ok(cases);
    }
}
