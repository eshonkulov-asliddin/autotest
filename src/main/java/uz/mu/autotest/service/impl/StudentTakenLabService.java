package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.mu.autotest.model.StudentTakenLab;
import uz.mu.autotest.repository.StudentTakenLabRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentTakenLabService {

    private final StudentTakenLabRepository userTakenLabRepo;

    public void save(StudentTakenLab studentTakenLab) {
        userTakenLabRepo.save(studentTakenLab);
    }

    public List<StudentTakenLab> getLabsByCourseIdAndUsername(Long courseId, String username) {
        log.info("course id: {}, username: {}", courseId, username);
        return userTakenLabRepo.findByCourseIdAndUsername(courseId, username);
    }

    public Optional<StudentTakenLab> getById(Long labId) {
        return userTakenLabRepo.findById(labId);
    }
}
