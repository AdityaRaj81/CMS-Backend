package com.legalcms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyResponse {

    private Long id;
    private String petitionerName;
    private String respondentName;
}
