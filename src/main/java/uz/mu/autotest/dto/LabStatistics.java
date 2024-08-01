package uz.mu.autotest.dto;

import uz.mu.autotest.dto.student.StudentDto;

import java.util.List;

public record LabStatistics(Long labId, Long groupId, List<StudentDto> completedStudents, List<StudentDto> pendingStudents, List<StudentDto> notStartedStudents) {
}
