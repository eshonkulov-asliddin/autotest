package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import uz.mu.autotest.dto.attempt.AttemptDto;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.repository.AttemptRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttemptService {

    private final AttemptRepository attemptRepo;
    private final ConversionService conversionService;

    public List<AttemptDto> getAttemptsByUserTakenLabId(Long userTakenLabId) {
        List<Attempt> byUserIdAndCourseId = attemptRepo.findByStudentTakenLabId(userTakenLabId);
        List<AttemptDto> attemptDtoList = byUserIdAndCourseId.stream()
                .map(attempt -> conversionService.convert(attempt, AttemptDto.class))
                .toList();
        log.info("Retrieved attempts by userTakenLabId {}", userTakenLabId);
        return attemptDtoList;
    }

    public void addAttempt(Attempt attempt) {
        log.info("Save attempt");
        attemptRepo.save(attempt);
    }

    public Optional<Attempt> getAttemptById(Long id) {
        Optional<Attempt> attempt = attemptRepo.findById(id);
        log.info("Retrieved attempt by id {}", id);
        return attempt;
    }
}
