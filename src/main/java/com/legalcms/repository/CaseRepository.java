package com.legalcms.repository;

import com.legalcms.model.CaseEntity;
import com.legalcms.model.CaseStatus;
import com.legalcms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository<CaseEntity, Long> {

    Optional<CaseEntity> findByCaseNumber(String caseNumber);

    Optional<CaseEntity> findByCnrNumber(String cnrNumber);

    List<CaseEntity> findByAssignedAdvocate(User advocate);

    List<CaseEntity> findByStatus(CaseStatus status);

    List<CaseEntity> findByCourtName(String courtName);

    @Query("SELECT c FROM CaseEntity c WHERE c.nextHearingDate BETWEEN :startDate AND :endDate")
    List<CaseEntity> findCasesWithHearingsBetween(
        @LocalDate startDate, 
        @LocalDate endDate
    );

    @Query("SELECT c FROM CaseEntity c JOIN c.parties p WHERE " +
           "LOWER(p.petitionerName) LIKE LOWER(CONCAT('%', :partyName, '%')) OR " +
           "LOWER(p.respondentName) LIKE LOWER(CONCAT('%', :partyName, '%'))")
    List<CaseEntity> findByPartyName(@Param("partyName") String partyName);

    @Query("SELECT c FROM CaseEntity c WHERE " +
           "(:caseNumber IS NULL OR c.caseNumber = :caseNumber) AND " +
           "(:cnrNumber IS NULL OR c.cnrNumber = :cnrNumber) AND " +
           "(:courtName IS NULL OR LOWER(c.courtName) LIKE LOWER(CONCAT('%', :courtName, '%')))")
    List<CaseEntity> searchCases(
        @Param("caseNumber") String caseNumber,
        @Param("cnrNumber") String cnrNumber,
        @Param("courtName") String courtName
    );

    List<CaseEntity> findByStatusAndCnrNumberIsNotNull(CaseStatus status);
}
