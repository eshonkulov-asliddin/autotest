package uz.mu.autotest.dto.lab;

import uz.mu.autotest.model.LabStatus;

public record LabResult(Long labId, String name, String url, LabStatus status){}
