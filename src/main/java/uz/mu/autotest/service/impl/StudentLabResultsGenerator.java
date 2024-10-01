package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.comparator.Comparators;
import uz.mu.autotest.dto.lab.LabResult;
import uz.mu.autotest.dto.lab.StudentLabResult;
import uz.mu.autotest.dto.lab.LabDto;
import uz.mu.autotest.model.LabStatus;
import uz.mu.autotest.model.Student;
import uz.mu.autotest.model.StudentTakenLab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudentLabResultsGenerator {

    private final StudentTakenLabService studentTakenLabService;

    public List<StudentLabResult> generateStudentsLabResults(Set<Student> students, List<LabDto> assignedLabs, Long courseId) {
        log.info("Students: {}", students);
        log.info("Assigned Labs: {}", assignedLabs);
        log.info("Course Id: {}", courseId);


        List<StudentLabResult> studentsLabResults = new ArrayList<>();
        for (Student student : students) {
            List<StudentTakenLab> studentTakenLabs = studentTakenLabService.getLabsByCourseIdAndUsername(courseId, student.getUsername());
            // labResults for NOT_STARTED labs
            List<LabResult> studentNotStartedLabs = getStudentNotStartedLabs(studentTakenLabs, assignedLabs);

            log.info("StudentTakenLabs: {}", studentTakenLabs);
            log.info("StudentNotStartedLabs: {}", studentNotStartedLabs);

            // labResults for STARTED labs
            List<LabResult> labResults = new ArrayList<>();
            for (StudentTakenLab studentTakenLab : studentTakenLabs) {
                Long labId = studentTakenLab.getLab().getId();
                String labName = studentTakenLab.getLab().getLabName();
                String githubUrl = studentTakenLab.getGithubUrl();
                LabStatus status = studentTakenLab.getStatus();
                LabResult labResult = new LabResult(labId, labName, githubUrl, status);
                labResults.add(labResult);
            }

            labResults.addAll(studentNotStartedLabs);
            labResults.sort(Comparator.comparingLong(LabResult::labId));
            StudentLabResult studentLabResult = new StudentLabResult(student.getUsername(), labResults);
            studentsLabResults.add(studentLabResult);
        }

        studentsLabResults.sort(Comparator.comparing(StudentLabResult::username));
        log.info("StudentLabResults: {}", studentsLabResults);
        return studentsLabResults;
    }


    private List<LabResult> getStudentNotStartedLabs(List<StudentTakenLab> studentTakenLabs, List<LabDto> assignedLabs) {
            Set<Long> studentTakenLabIds = studentTakenLabs.stream().map(s -> s.getLab().getId()).collect(Collectors.toSet());
            List<LabResult> labResults = new ArrayList<>();
            for (LabDto assignedLab : assignedLabs) {
                if (!studentTakenLabIds.contains(assignedLab.id())) {
                    Long labId = assignedLab.id();
                    String labName = assignedLab.name();
                    String githubUrl = assignedLab.githubUrl();
                    LabStatus status = LabStatus.NOT_STARTED;
                    labResults.add(new LabResult(labId, labName, githubUrl, status));
                }
            }
            log.info("NotStartedLabs: {}", labResults);
            return labResults;
    }

}
