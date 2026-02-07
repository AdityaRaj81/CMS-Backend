package com.legalcms.controller;

import com.legalcms.dto.HearingResponse;
import com.legalcms.service.HearingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hearings")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Hearing Management", description = "Endpoints for managing case hearings")
public class HearingController {

    private final HearingService hearingService;

    @GetMapping("/case/{caseId}")
    @Operation(summary = "Get hearing history", description = "Returns all hearings for a specific case")
    public ResponseEntity<List<HearingResponse>> getHearingsByCaseId(@PathVariable Long caseId) {
        List<HearingResponse> hearings = hearingService.getHearingsByCaseId(caseId);
        return ResponseEntity.ok(hearings);
    }
}
