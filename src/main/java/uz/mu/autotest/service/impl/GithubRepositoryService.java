package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.dto.GenerateRepositoryRequest;
import uz.mu.autotest.dto.StudentTakenLabDto;
import uz.mu.autotest.exception.GenerateRepositoryException;
import uz.mu.autotest.exception.NotFoundException;
import uz.mu.autotest.model.Lab;
import uz.mu.autotest.model.LabStatus;
import uz.mu.autotest.model.StudentTakenLab;
import uz.mu.autotest.service.RepositoryService;
import uz.mu.autotest.utils.GithubUrlParser;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubRepositoryService implements RepositoryService {

    private final LabService labService;
    private final AuthService authService;
    private final GithubClient githubClient;
    private final UserService userService;
    private final StudentService studentService;
    private final StudentTakenLabService studentTakenLabService;

    @Transactional
    public StudentTakenLabDto generateRepository(GenerateRepositoryRequest request, String labOwner, String accessToken) {
        Long labId = request.labId();
        if (labId == null ) throw new IllegalArgumentException();
        log.info("generate repository called for lab id: {}", labId);

        Optional<Lab> lab = labService.getById(labId);

        if (lab.isEmpty()) {
            throw new NotFoundException(String.format("Lab with id %s doesn't exist", labId));
        }

        String repoUrl = lab.get().getGithubUrl();
        String templateRepoOwner = GithubUrlParser.extractUsername(repoUrl);
        String templateRepo = GithubUrlParser.extractRepoName(repoUrl);
        String description = "Lab " + lab.get().getId();
        boolean doIncludeAllBranch = false;
        boolean isPrivate = false;

        ResponseEntity<String> genereateRepositoryResponse = githubClient.generateRepository(labOwner, templateRepoOwner, templateRepo, description, doIncludeAllBranch, isPrivate, accessToken);
        log.info("Generate repository response: {}", genereateRepositoryResponse);

        if (genereateRepositoryResponse.getStatusCode() == HttpStatus.CREATED) {
            String labGithubUrl = "https://github.com" + "/" + labOwner + "/" + templateRepo;
            StudentTakenLab studentTakenLab = new StudentTakenLab();
            studentTakenLab.setGithubUrl(labGithubUrl);
            studentTakenLab.setStatus(LabStatus.STARTED);
            studentTakenLab.setCourse(lab.get().getCourse());
            studentTakenLab.setLab(lab.get());
            log.info("labOwner: {}", labOwner);
            studentTakenLab.setUser(studentService.getStudentByOAuth2Login(labOwner).get());
            studentTakenLabService.save(studentTakenLab);

            log.info("Insert new Lab: {}", studentTakenLab);

            return new StudentTakenLabDto(studentTakenLab.getId(), studentTakenLab.getGithubUrl(), String.valueOf(studentTakenLab.getStatus()), studentTakenLab.getCourse().getId(), studentTakenLab.getLab().getId(), studentTakenLab.getUser().getId());
        }
        throw new GenerateRepositoryException("Failed to generate repository");
    }
}
