package uz.mu.autotest.dto;

public record StudentTakenLabDto(
       Long id,
       String githubUrl,
       String status,
       Long courseId,
       Long labId,
       Long userId ) { }
