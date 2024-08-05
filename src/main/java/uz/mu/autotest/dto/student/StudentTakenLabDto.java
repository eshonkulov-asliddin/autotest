package uz.mu.autotest.dto.student;

public record StudentTakenLabDto(
       Long id,
       String githubUrl,
       String status,
       Long courseId,
       Long labId,
       Long userId ) { }
