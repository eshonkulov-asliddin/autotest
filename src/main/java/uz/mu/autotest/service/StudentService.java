package uz.mu.autotest.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.mu.autotest.dto.student.AddStudentRequest;
import uz.mu.autotest.dto.student.StudentDto;
import uz.mu.autotest.model.Group;
import uz.mu.autotest.model.Role;
import uz.mu.autotest.model.Student;
import uz.mu.autotest.repository.StudentRepository;
import uz.mu.autotest.security.credential.CredentialGenerator;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final CredentialGenerator credentialGenerator;
    private final PasswordEncoder passwordEncoder;
    private final GroupService groupService;
    private final ConversionService conversionService;

    public void addStudent(AddStudentRequest addStudentRequest){
        String username = credentialGenerator.generateUsername(addStudentRequest.firstName(), addStudentRequest.lastName());
        String password = credentialGenerator.generatePassword();

        Student student = new Student();
        student.setFirstName(addStudentRequest.firstName());
        student.setLastName(addStudentRequest.lastName());
        student.setUsername(username);
        student.setPassword(passwordEncoder.encode(password));
        student.setPassword(password);
        student.setRoles(Set.of(Role.STUDENT));
        student.setActive(true);
        Group group = groupService.getGroupById(addStudentRequest.groupIds());
        group.addStudent(student);
        studentRepository.save(student);
    }

    public Page<StudentDto> getAllStudents(Pageable pageable){
        Page<Student> students = studentRepository.findAll(pageable);
        List<StudentDto> studentDtos = students.getContent().stream()
                .map(student -> conversionService.convert(student, StudentDto.class))
                .toList();
        return new PageImpl<>(studentDtos, pageable, students.getTotalElements());
    }

    public void updateStudent(Student student) {
        Student studentById = getById(student.getId());
        // update firstName and lastName
        studentById.setFirstName(student.getFirstName());
        studentById.setLastName(student.getLastName());
        studentRepository.save(studentById);
    }

    public void deleteStudent(Long id) {
        Student student = getById(id);
        student.getGroups().forEach(group -> group.removeStudent(student));
        studentRepository.delete(student);
    }

    public Page<StudentDto> getStudentsByGroupId(Long groupId, Pageable pageable) {
        Page<Student> students = studentRepository.findStudentsByGroupId(groupId, pageable);
        List<StudentDto> studentDtos = students.getContent().stream()
                .map(student -> conversionService.convert(student, StudentDto.class))
                .toList();

        return new PageImpl<>(studentDtos, pageable, students.getTotalElements());

    }

    public Optional<Student> getStudentByOAuth2Login(String loginName) {
        Optional<Student> byOAuth2Login = studentRepository.findByLogin(loginName);
        log.info("Student by oauth2 login {}: {}", loginName, byOAuth2Login);
        return byOAuth2Login;
    }

    public Student getById(Long id) {
        Optional<Student> studentById = studentRepository.findById(id);
        if (studentById.isEmpty()) {
            throw new EntityNotFoundException("Student not found with id: " + id);
        }

        return studentById.get();
    }

    public Long getCount() {
        log.info("Getting students count...");
        return studentRepository.count();
    }
}
