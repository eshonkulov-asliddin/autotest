package uz.mu.autotest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.repository.AttemptRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttemptService {

    private final AttemptRepository attemptRepo;

    public List<Attempt> getAttemptsByUserTakenLabId(Long userTakenLabId) {
        List<Attempt> byUserIdAndCourseId = attemptRepo.findByUserTakenLabId(userTakenLabId);
        log.info("Retrieved attempts by userTakenLabId {}", userTakenLabId);
        return byUserIdAndCourseId;
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
