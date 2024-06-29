package uz.mu.autotest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mu.autotest.client.GithubClient;
import uz.mu.autotest.dto.SubmitTaskRequest;
import uz.mu.autotest.dto.UserTakenLabDto;
import uz.mu.autotest.exception.GenerateRepositoryException;
import uz.mu.autotest.exception.NotFoundException;
import uz.mu.autotest.model.Lab;
import uz.mu.autotest.model.LabStatus;
import uz.mu.autotest.model.UserTakenLab;
import uz.mu.autotest.service.AuthService;
import uz.mu.autotest.service.LabService;
import uz.mu.autotest.service.RepositoryService;
import uz.mu.autotest.service.StudentService;
import uz.mu.autotest.service.UserService;
import uz.mu.autotest.service.UserTakenLabService;
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
    private final UserTakenLabService userTakenLabService;

    public UserTakenLabDto generateRepository(SubmitTaskRequest request, String labOwner, String accessToken) {
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
            UserTakenLab userTakenLab = new UserTakenLab();
            userTakenLab.setGithubUrl(labGithubUrl);
            userTakenLab.setStatus(LabStatus.STARTED);
            userTakenLab.setCourse(lab.get().getCourse());
            userTakenLab.setLab(lab.get());
            log.info("labOwner: {}", labOwner);
            userTakenLab.setUser(studentService.getStudentByOAuth2Login(labOwner).get());
            userTakenLabService.save(userTakenLab);

            log.info("Insert new Lab: {}", userTakenLab);

            return new UserTakenLabDto(userTakenLab.getId(), userTakenLab.getGithubUrl(), String.valueOf(userTakenLab.getStatus()), userTakenLab.getCourse().getId(), userTakenLab.getLab().getId(), userTakenLab.getUser().getId());
        }
        throw new GenerateRepositoryException("Failed to generate repository");
    }
}
