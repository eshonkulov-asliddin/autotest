package uz.mu.autotest.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.mu.autotest.model.UserTakenLab;
import uz.mu.autotest.repository.UserTakenLabRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserTakenLabService {

    private static final Logger log = LoggerFactory.getLogger(UserTakenLabService.class);
    private final UserTakenLabRepository userTakenLabRepo;

    public void save(UserTakenLab userTakenLab) {
        userTakenLabRepo.save(userTakenLab);
    }

    public List<UserTakenLab> getLabsByCourseIdAndUserUsername(Long courseId, String username) {
        log.info("course id: {}, username: {}", courseId, username);
        return userTakenLabRepo.findByCourseIdAndUserUsername(courseId, username);
    }

    public Optional<UserTakenLab> getById(Long labId) {
        return userTakenLabRepo.findById(labId);
    }
}
