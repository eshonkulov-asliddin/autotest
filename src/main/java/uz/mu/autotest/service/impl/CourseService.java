package uz.mu.autotest.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import uz.mu.autotest.dto.course.AddCourseRequest;
import uz.mu.autotest.dto.course.CourseDto;
import uz.mu.autotest.dto.course.UpdateCourseRequest;
import uz.mu.autotest.dto.group.GroupDto;
import uz.mu.autotest.exception.NotFoundException;
import uz.mu.autotest.model.Course;
import uz.mu.autotest.model.Group;
import uz.mu.autotest.model.Teacher;
import uz.mu.autotest.repository.CourseRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final ConversionService conversionService;
    private final TeacherService teacherService;
    private final GroupService groupService;

    public List<CourseDto> getAllCourses() {
        log.info("Retrieving all courses ....");
        List<Course> allCourses = courseRepository.findAll();
        return allCourses.stream()
                .map(course -> conversionService.convert(course, CourseDto.class))
                .toList();
    }

    public Optional<Course> getByName(String courseName) {
        Optional<Course> course = courseRepository.findByName(courseName);
        log.info("Retrieved course by name: {}", course);
        return course;
    }

    public Long getCount() {
        log.info("Getting courses count....");
        return courseRepository.count();
    }

    public void addCourse(AddCourseRequest request) {
        log.info("add course request: {}", request);
        Optional<Teacher> teacher = teacherService.getByUsername(request.teacherUsername());
        if (teacher.isEmpty()) {
            throw new NotFoundException("Failed to add new course, teacher must exist");
        }
        Course course = conversionService.convert(request, Course.class);
        course.setTeacher(teacher.get());
        courseRepository.save(course);
        log.info("Successfully added new course: {}", request.name());
    }

    public void update(Long id, UpdateCourseRequest updateCourseRequest) {
        Optional<Course> courseById = courseRepository.findById(id);
        if (courseById.isEmpty()) {
            throw new NotFoundException(String.format("Course with id %s not found", id));
        }

        Optional<Teacher> teacher = teacherService.getByUsername(updateCourseRequest.teacherUsername());
        if (teacher.isEmpty()) {
            throw new NotFoundException(String.format("Teacher with id %s not found", id));

        }
        Course course = courseById.get();
        course.setName(updateCourseRequest.name());
        course.setTeacher(teacher.get());
        courseRepository.save(course);
    }

    public void delete(Long id) {
        Course course = getById(id);
        courseRepository.delete(course);
    }

    public Course getById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new NotFoundException(String.format("Course with id % s not found", id));
        }
        return course.get();
    }

    public List<Course> getCoursesByTeacherUsername(String username) {
        log.info("Getting courses by teacher username {}", username);
        return courseRepository.findByTeacherUsername(username);
    }

    public CourseDto getCourseById(Long id) {
        log.info("Getting course by id: {}", id);
        Course course = getById(id);
        return conversionService.convert(course, CourseDto.class);
    }

    public void assignGroupsFor(Long courseId, List<Long> groupIds) {
        log.info("Assigning groups to course id: {}", courseId);
        Course course = getById(courseId);
        List<Group> groups = groupService.getGroupsByIds(groupIds);
        groups.forEach(group -> group.addCourse(course));
        courseRepository.save(course);
    }

    public void unassignGroupsFor(Long courseId, List<Long> groupIds) {
        log.info("Deassigning groups to course id: {}", courseId);
        Course course = getById(courseId);
        List<Group> groups = groupService.getGroupsByIds(groupIds);
        groups.forEach(group -> group.removeCourse(course));
        courseRepository.save(course);
    }

    public List<Course> getCourseByTeacherUsernameAndCourseId(String username, Long courseId) {
        log.info("Getting Course by teacher username {} and course id: {}", username, courseId);
        Optional<Course> course = courseRepository.findByIdAndTeacherUsername(courseId, username);
        if (course.isEmpty()) {
            throw new NotFoundException(String.format("Course with id %s for teacher %s not found", courseId, username));
        }
        return List.of(course.get());
    }

    public List<GroupDto> getGroups(List<Course> courses) {
        return courses.stream()
                .map(Course::getGroups)
                .flatMap(Collection::stream)
                .map(group -> conversionService.convert(group, GroupDto.class))
                .toList();
    }
}
