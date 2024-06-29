package uz.mu.autotest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mu.autotest.model.Attempt;
import uz.mu.autotest.model.CustomOAuth2User;
import uz.mu.autotest.model.Lab;
import uz.mu.autotest.model.UserTakenLab;
import uz.mu.autotest.service.AttemptService;
import uz.mu.autotest.service.LabService;
import uz.mu.autotest.service.ReadmeService;
import uz.mu.autotest.service.UserTakenLabService;
import uz.mu.autotest.utils.GithubUrlParser;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static uz.mu.autotest.controller.util.ApiConst.ADMIN_PREFIX;
import static uz.mu.autotest.controller.util.ApiConst.LABS_ENDPOINT;

@Controller
@AllArgsConstructor
@RequestMapping(path = ADMIN_PREFIX +LABS_ENDPOINT)
@Slf4j
public class LabController {

    private final LabService labService;
    private final UserTakenLabService userTakenLabService;
    private final AttemptService attemptService;
    private final ReadmeService readmeService;

    @GetMapping("/courses/{courseId}")
    public String getLabsByCourseId(@PathVariable(name = "courseId") Long courseId, Model model) {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String userName = oAuth2User.getLogin();

        List<UserTakenLab> userTakenLabs = userTakenLabService.getLabsByCourseIdAndUserUsername(courseId, userName);
        List<Lab> labsByCourseId = labService.getLabsByCourseId(courseId);

        log.info("userTakenLabs: {}", userTakenLabs);

        // Extract lab IDs from labs taken by users
        Set<Long> takenLabIds = userTakenLabs.stream()
                .map(userTakenLab -> userTakenLab.getLab().getId())
                .collect(Collectors.toSet());

        // Filter out labs that are not taken by users
        List<Lab> remainingLabs = labsByCourseId.stream()
                .filter(lab -> !takenLabIds.contains(lab.getId()))
                .toList();

        log.info("notStartedLabs: {}", remainingLabs);

        model.addAttribute("labs", remainingLabs);
        model.addAttribute("takenLabs", userTakenLabs);
        return "labs.html";
    }

    @GetMapping("/{labId}/start")
    public String getNotStartedLabById(@PathVariable(name= "labId") Long labId, Model model) {
        Optional<Lab> notStartedLab = labService.getById(labId);
        Optional<UserTakenLab> userTakenLab = userTakenLabService.getById(labId);

        if (userTakenLab.isPresent()) {
            String readmeUrl = GithubUrlParser.generateReadmeUrl(userTakenLab.get().getLab().getGithubUrl());
            String readmeHtml = readmeService.getReadmeHtml(readmeUrl);
            log.info("Retrieved Started Lab: {}", userTakenLab.get());
            List<Attempt> attemptsByUserTakenLabId = attemptService.getAttemptsByUserTakenLabId(userTakenLab.get().getId());
            log.info("Retrieved Attemps: {}", attemptsByUserTakenLabId);
            model.addAttribute("lab", userTakenLab.get());
            model.addAttribute("attempts", attemptsByUserTakenLabId);
            model.addAttribute("readmeHtml", readmeHtml);
        }else {
            String readmeUrl = GithubUrlParser.generateReadmeUrl(notStartedLab.get().getGithubUrl());
            String readmeHtml = readmeService.getReadmeHtml(readmeUrl);
            log.info("Retrieved Lab: {}", notStartedLab.get());
            model.addAttribute("lab", notStartedLab.get());
            model.addAttribute("readmeHtml", readmeHtml);
        }

        return "lab.html";
    }
}
