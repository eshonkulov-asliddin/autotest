package uz.mu.autotest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.mu.autotest.model.Lab;
import uz.mu.autotest.repository.LabRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabService {

    private final LabRepository labRepository;
    private final String PREFIX = "https://raw.githubusercontent.com/";

    public List<Lab> getLabsByCourseId(Long courseId) {
        List<Lab> labsByCourseId = labRepository.findLabsByCourseId(courseId);
        log.info("Retrieved labs for course id {}, {}", courseId, labsByCourseId);
        return labsByCourseId;
    }

    public Optional<Lab> getById(Long labId) {
        Optional<Lab> labById = labRepository.findById(labId);
        log.info("Retrieved lab with id {}", labId);
        return labById;
    }

}
