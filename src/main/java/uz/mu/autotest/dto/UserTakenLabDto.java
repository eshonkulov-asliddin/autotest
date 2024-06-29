package uz.mu.autotest.dto;

public record UserTakenLabDto (
       Long id,
       String githubUrl,
       String status,
       Long courseId,
       Long labId,
       Long userId ) { }
