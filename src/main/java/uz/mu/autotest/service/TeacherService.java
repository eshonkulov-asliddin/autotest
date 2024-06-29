package uz.mu.autotest.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.mu.autotest.dto.teacher.AddTeacherRequest;
import uz.mu.autotest.dto.teacher.TeacherDto;
import uz.mu.autotest.model.Role;
import uz.mu.autotest.model.Teacher;
import uz.mu.autotest.repository.TeacherRepository;
import uz.mu.autotest.security.credential.CredentialGenerator;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CredentialGenerator credentialGenerator;
    private final PasswordEncoder passwordEncoder;
    private final ConversionService conversionService;
    private final GroupService groupService;


    public void addTeacher(AddTeacherRequest request) {
        String username = credentialGenerator.generateUsername(request.firstName(), request.lastName());
        String password = credentialGenerator.generatePassword();

        Teacher teacher = conversionService.convert(request, Teacher.class);
        teacher.setUsername(username);
        teacher.setPassword(passwordEncoder.encode(password));
        teacher.setPassword(password);
        teacher.setRoles(Set.of(Role.TEACHER));
        teacher.setActive(true);
        teacherRepository.save(teacher);
    }

    public List<TeacherDto> getAllTeachers(){
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream()
                .map(teacher -> conversionService.convert(teacher, TeacherDto.class))
                .toList();
    }

    public Long getCount() {
        log.info("Getting teachers count...");
        return teacherRepository.count();
    }

    public void updateTeacher(Teacher teacher) {
        Teacher teacherById = getById(teacher.getId());
        // update firstName and lastName
        teacherById.setFirstName(teacher.getFirstName());
        teacherById.setLastName(teacher.getLastName());
        teacherRepository.save(teacherById);
    }

    public void deleteById(Long id) {
        Teacher teacher = getById(id);
        teacherRepository.delete(teacher);
    }

    public TeacherDto getTeacherById(Long id) {
        Teacher teacher = getById(id);
        return conversionService.convert(teacher, TeacherDto.class);
    }

    private Teacher getById(Long id) {
        Optional<Teacher> teacherById = teacherRepository.findById(id);
        if (teacherById.isEmpty()) {
            throw new EntityNotFoundException("Teacher not found with id: " + id);
        }
        return teacherById.get();
    }

    public Optional<Teacher> getByUsername(String username) {
        log.info("Getting teacher by username: {}", username);
        return teacherRepository.findByUsername(username);
    }
}
