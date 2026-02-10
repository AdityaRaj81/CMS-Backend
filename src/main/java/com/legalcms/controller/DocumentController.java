package com.legalcms.controller;

import com.legalcms.dto.DocumentResponse;
import com.legalcms.model.Document;
import com.legalcms.model.DocumentType;
import com.legalcms.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Document Management", description = "Endpoints for uploading and managing case documents")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADVOCATE')")
    @Operation(summary = "Upload document", description = "Upload PDF document and link to case")
    public ResponseEntity<DocumentResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("caseId") Long caseId,
            @RequestParam("documentType") DocumentType documentType,
            Authentication authentication) {
        String email = authentication.getName();
        DocumentResponse response = documentService.uploadDocument(caseId, file, documentType, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all documents", description = "Returns all documents in the system")
    public ResponseEntity<List<DocumentResponse>> getAllDocuments() {
        List<DocumentResponse> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/case/{caseId}")
    @Operation(summary = "Get documents by case", description = "Returns all documents linked to a specific case")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByCaseId(@PathVariable Long caseId) {
        List<DocumentResponse> documents = documentService.getDocumentsByCaseId(caseId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/view/{documentId}")
    @Operation(summary = "View document", description = "Stream PDF document for in-app viewing")
    public ResponseEntity<Resource> viewDocument(@PathVariable Long documentId) {
        try {
            Document document = documentService.getDocumentById(documentId);
            Path filePath = Paths.get(document.getFileUrl());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + document.getFileName() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found or not readable");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error retrieving document: " + e.getMessage());
        }
    }
}
