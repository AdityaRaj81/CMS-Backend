package com.legalcms.scheduler;

import com.legalcms.dto.CaseStatusResponse;
import com.legalcms.model.CaseEntity;
import com.legalcms.service.CaseService;
import com.legalcms.service.CourtIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Court Sync Scheduler
 * 
 * This scheduler runs daily to sync case data from court systems.
 * It fetches the latest information for all active cases with CNR numbers.
 * 
 * Features:
 * - Updates next hearing dates automatically
 * - Updates case stage/status
 * - Logs all changes for audit trail
 * - Can be enabled/disabled via application.yml
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(value = "app.court-sync.enabled", havingValue = "true", matchIfMissing = true)
public class CourtSyncScheduler {

    private final CaseService caseService;
    private final CourtIntegrationService courtIntegrationService;

    /**
     * Scheduled job that runs once per day at 2 AM
     * Cron expression: "0 0 2 * * ?" = second minute hour day month weekday
     */
    @Scheduled(cron = "${app.court-sync.cron:0 0 2 * * ?}")
    public void syncCourtData() {
        log.info("=== Court Sync Scheduler Started at {} ===", LocalDateTime.now());

        try {
            List<CaseEntity> activeCases = caseService.getActiveCasesWithCnr();
            log.info("Found {} active cases with CNR numbers to sync", activeCases.size());

            int successCount = 0;
            int failureCount = 0;

            for (CaseEntity caseEntity : activeCases) {
                try {
                    syncSingleCase(caseEntity);
                    successCount++;
                } catch (Exception e) {
                    log.error("Failed to sync case ID: {} - {}", caseEntity.getId(), e.getMessage());
                    failureCount++;
                }
            }

            log.info("=== Court Sync Completed: {} successful, {} failed ===", successCount, failureCount);

        } catch (Exception e) {
            log.error("Court sync scheduler encountered an error", e);
        }
    }

    private void syncSingleCase(CaseEntity caseEntity) {
        log.debug("Syncing case ID: {} with CNR: {}", caseEntity.getId(), caseEntity.getCnrNumber());

        // Fetch latest status from court system
        CaseStatusResponse courtStatus = courtIntegrationService.fetchCaseStatus(caseEntity.getCnrNumber());

        // Check if data has changed
        boolean hasChanges = false;

        if (courtStatus.getNextHearingDate() != null && 
            !courtStatus.getNextHearingDate().equals(caseEntity.getNextHearingDate())) {
            log.info("Case {} - Next hearing date changed from {} to {}", 
                    caseEntity.getCaseNumber(), 
                    caseEntity.getNextHearingDate(), 
                    courtStatus.getNextHearingDate());
            hasChanges = true;
        }

        if (courtStatus.getCaseStage() != null && 
            !courtStatus.getCaseStage().equals(caseEntity.getCaseStage())) {
            log.info("Case {} - Stage changed from {} to {}", 
                    caseEntity.getCaseNumber(), 
                    caseEntity.getCaseStage(), 
                    courtStatus.getCaseStage());
            hasChanges = true;
        }

        // Update case if changes detected
        if (hasChanges) {
            caseService.updateCaseDetails(
                caseEntity.getId(), 
                courtStatus.getCaseStage(), 
                courtStatus.getNextHearingDate()
            );
            log.info("Successfully updated case ID: {}", caseEntity.getId());
        } else {
            log.debug("No changes detected for case ID: {}", caseEntity.getId());
        }
    }

    /**
     * Manual trigger endpoint for testing
     * Can be called via admin API if needed
     */
    public void triggerManualSync() {
        log.info("Manual court sync triggered");
        syncCourtData();
    }
}
