package com.legalcms.service;

import com.legalcms.dto.DocumentResponse;
import com.legalcms.model.CaseEntity;
import com.legalcms.model.Document;
import com.legalcms.model.DocumentType;
import com.legalcms.model.User;
import com.legalcms.repository.CaseRepository;
import com.legalcms.repository.DocumentRepository;
import com.legalcms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/documents/";

    @Transactional
    public DocumentResponse uploadDocument(Long caseId, MultipartFile file, 
                                          DocumentType documentType, String userEmail) {
        log.info("Uploading document for case ID: {}", caseId);

        CaseEntity caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".pdf";
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Save file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Save document record
            Document document = Document.builder()
                    .caseEntity(caseEntity)
                    .fileName(originalFilename)
                    .fileUrl(UPLOAD_DIR + uniqueFilename)
                    .documentType(documentType)
                    .uploadedBy(user)
                    .build();

            document = documentRepository.save(document);
            log.info("Document uploaded successfully with ID: {}", document.getId());

            return mapToDocumentResponse(document);
        } catch (IOException e) {
            log.error("Error uploading document", e);
            throw new RuntimeException("Failed to upload document: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> getDocumentsByCaseId(Long caseId) {
        log.info("Fetching documents for case ID: {}", caseId);
        CaseEntity caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        List<Document> documents = documentRepository.findByCaseEntity(caseEntity);
        return documents.stream()
                .map(this::mapToDocumentResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Document getDocumentById(Long documentId) {
        log.info("Fetching document by ID: {}", documentId);
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    private DocumentResponse mapToDocumentResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .caseId(document.getCaseEntity().getId())
                .fileName(document.getFileName())
                .fileUrl(document.getFileUrl())
                .documentType(document.getDocumentType())
                .uploadedByName(document.getUploadedBy().getFullName())
                .uploadedAt(document.getUploadedAt())
                .build();
    }
}
