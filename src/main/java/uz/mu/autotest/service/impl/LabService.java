package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import uz.mu.autotest.dto.lab.AddLabRequest;
import uz.mu.autotest.dto.lab.LabDto;
import uz.mu.autotest.dto.lab.LabStatistics;
import uz.mu.autotest.dto.student.StudentDto;
import uz.mu.autotest.exception.AccessDeniedException;
import uz.mu.autotest.model.Course;
import uz.mu.autotest.model.Lab;
import uz.mu.autotest.model.LabStatus;
import uz.mu.autotest.model.StudentTakenLab;
import uz.mu.autotest.repository.LabRepository;
import uz.mu.autotest.repository.StudentTakenLabRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabService {

    private final LabRepository labRepository;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final ConversionService conversionService;
    private final StudentTakenLabRepository studentTakenLabRepository;

    public List<LabDto> getLabsByCourseId(String username, Long courseId) {
        // TODO think about verifyAccess(username, courseId);
        List<Lab> labsByCourseId = labRepository.findLabsByCourseId(courseId);
        log.info("Retrieved labs for course id {}, {}", courseId, labsByCourseId);
        return labsByCourseId.stream()
                .map(lab -> conversionService.convert(lab, LabDto.class))
                .toList();
    }

    public Optional<Lab> getById(Long labId) {
        Optional<Lab> labById = labRepository.findById(labId);
        log.info("Retrieved lab with id {}", labId);
        return labById;
    }

    public void addLab(String username, Long courseId, AddLabRequest request) {
        verifyAccess(username, courseId);
        log.info("Teacher with username {} adding lab to course id {}", username, courseId);
        Course course = courseService.getById(courseId);
        Lab lab = new Lab();
        lab.setLabName(request.labName());
        lab.setGithubUrl(request.githubUrl());
        lab.setStatus(LabStatus.NOT_STARTED);
        lab.setCourse(course);

        labRepository.save(lab);
    }

    public LabStatistics getLabStatisticsForGroup(Long labId, Long groupId) {
        List<StudentTakenLab> studentTakenLabByLabIdAndGroupId = studentTakenLabRepository.findStudentTakenLabByLabIdAndGroupId(labId, groupId);

        List<StudentDto> completedStudents = studentTakenLabByLabIdAndGroupId.stream()
                .filter(studentTakenLab -> studentTakenLab.getStatus().equals(LabStatus.COMPLETED))
                .map(StudentTakenLab::getUser)
                .map(user -> conversionService.convert(user, StudentDto.class))
                .toList();

        List<StudentDto> pendingStudents = studentTakenLabByLabIdAndGroupId.stream()
                .filter(studentTakenLab -> studentTakenLab.getStatus().equals(LabStatus.STARTED))
                .map(StudentTakenLab::getUser)
                .map(user -> conversionService.convert(user, StudentDto.class))
                .toList();

        List<StudentDto> notStartedStudents = studentTakenLabByLabIdAndGroupId.stream()
                .filter(studentTakenLab -> studentTakenLab.getStatus().equals(LabStatus.NOT_STARTED))
                .map(StudentTakenLab::getUser)
                .map(user -> conversionService.convert(user, StudentDto.class))
                .toList();
        LabStatistics labStatistics = new LabStatistics(labId, groupId, completedStudents, pendingStudents, notStartedStudents);
        log.info("Statistics for labId {} groupId {}: {}", labId, groupId, labStatistics);

        return labStatistics;
    }

    public void verifyAccess(String username, Long courseId) {
        Course course = courseService.getById(courseId);
        if (!course.getTeacher().getUsername().equals(username)) {
            throw new AccessDeniedException(String.format("Teacher with username %s does not have rights to access course id: %s", username, courseId));
        }
    }
}
