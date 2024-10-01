package uz.mu.autotest.dto.lab;

import uz.mu.autotest.dto.lab.LabResult;

import java.util.List;

public record StudentLabResult(String username, List<LabResult> labs){}

