package com.legalcms.repository;

import com.legalcms.model.Document;
import com.legalcms.model.CaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByCaseEntity(CaseEntity caseEntity);
}
