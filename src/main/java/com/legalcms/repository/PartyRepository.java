package com.legalcms.repository;

import com.legalcms.model.Party;
import com.legalcms.model.CaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {

    List<Party> findByCaseEntity(CaseEntity caseEntity);
}
