package uz.mu.autotest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uz.mu.autotest.dto.attempt.AttemptDto;
import uz.mu.autotest.dto.lab.LabDto;
import uz.mu.autotest.dto.lab.LabStatistics;
import uz.mu.autotest.exception.NotFoundException;
import uz.mu.autotest.model.Lab;
import uz.mu.autotest.model.StudentTakenLab;
import uz.mu.autotest.service.impl.AttemptService;
import uz.mu.autotest.service.impl.LabService;
import uz.mu.autotest.service.impl.ReadmeService;
import uz.mu.autotest.service.impl.StudentTakenLabService;
import uz.mu.autotest.utils.GithubUrlParser;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static uz.mu.autotest.controller.util.ApiConst.LABS_ENDPOINT;

@Controller
@AllArgsConstructor
@RequestMapping(path = LABS_ENDPOINT)
@Slf4j
public class LabController {

    private final LabService labService;
    private final StudentTakenLabService studentTakenLabService;
    private final AttemptService attemptService;
    private final ReadmeService readmeService;

    @PreAuthorize("hasRole('STUDENT') or hasAuthority('OAUTH2_USER')")
    @GetMapping("/courses/{courseId}")
    public String getLabsByCourseId(@PathVariable(name = "courseId") Long courseId, Principal principal, Model model) {
        String username = principal.getName();
        List<StudentTakenLab> studentTakenLabs = studentTakenLabService.getLabsByCourseIdAndUsername(courseId, username);
        List<LabDto> labsByCourseId = labService.getLabsByCourseId(username, courseId);

        log.info("userTakenLabs: {}", studentTakenLabs);

        // Extract lab IDs from labs taken by users
        Set<Long> takenLabIds = studentTakenLabs.stream()
                .map(userTakenLab -> userTakenLab.getLab().getId())
                .collect(Collectors.toSet());

        // Filter out labs that are not taken by users
        List<LabDto> remainingLabs = labsByCourseId.stream()
                .filter(lab -> !takenLabIds.contains(lab.id()))
                .toList();

        log.info("notStartedLabs: {}", remainingLabs);

        model.addAttribute("labs", remainingLabs);
        model.addAttribute("takenLabs", studentTakenLabs);
        return "labs.html";
    }

    @PreAuthorize("hasRole('STUDENT') or hasAuthority('OAUTH2_USER')")
    @GetMapping("/{labId}/start")
    public String getNotStartedLabById(@PathVariable(name= "labId") Long labId, Model model) {
        Optional<StudentTakenLab> userTakenLab = studentTakenLabService.getById(labId);

        if (userTakenLab.isPresent()) {
            String githubUrl = userTakenLab.get().getLab().getGithubUrl();
            String repoName = GithubUrlParser.extractRepoName(githubUrl);
            String readmeUrl = GithubUrlParser.generateReadmeUrl(githubUrl);
            String readmeHtml = readmeService.getReadmeHtml(readmeUrl);
            log.info("Retrieved Started Lab: {}", userTakenLab.get());
            List<AttemptDto> attemptsByUserTakenLabId = attemptService.getAttemptsByUserTakenLabId(userTakenLab.get().getId());
            log.info("Retrieved Attempts: {}", attemptsByUserTakenLabId);
            model.addAttribute("lab", userTakenLab.get());
            model.addAttribute("attempts", attemptsByUserTakenLabId);
            model.addAttribute("readmeHtml", readmeHtml);
            model.addAttribute("repoName", repoName);
        }else {
            Optional<Lab> notStartedLab = labService.getById(labId);
            if (notStartedLab.isEmpty()) {
                throw new NotFoundException(String.format("Lab with id {} NOT FOUND", labId));
            }
            String githubUrl = notStartedLab.get().getGithubUrl();
            String repoName = GithubUrlParser.extractRepoName(githubUrl);
            String readmeUrl = GithubUrlParser.generateReadmeUrl(githubUrl);
            String readmeHtml = readmeService.getReadmeHtml(readmeUrl);
            log.info("Retrieved Lab: {}", notStartedLab.get());
            model.addAttribute("lab", notStartedLab.get());
            model.addAttribute("readmeHtml", readmeHtml);
            model.addAttribute("repoName", repoName);
        }

        return "lab.html";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{id}/statistics")
    @ResponseBody
    public LabStatistics getLabStatistics(@PathVariable("id") Long labId,
                                                @RequestParam("groupId") Long groupId) {
        return labService.getLabStatisticsForGroup(labId, groupId);
    }
}
