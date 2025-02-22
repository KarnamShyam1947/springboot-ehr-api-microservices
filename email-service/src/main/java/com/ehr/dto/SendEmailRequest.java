package com.ehr.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {
    private String to;
    private String from;
    private String subject;
    private String templateName;
    private Map<String, Object> map;
}
