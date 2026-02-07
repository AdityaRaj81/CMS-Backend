package com.legalcms.dto;

import com.legalcms.model.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {

    private Long id;
    private Long caseId;
    private String fileName;
    private String fileUrl;
    private DocumentType documentType;
    private String uploadedByName;
    private LocalDateTime uploadedAt;
}
