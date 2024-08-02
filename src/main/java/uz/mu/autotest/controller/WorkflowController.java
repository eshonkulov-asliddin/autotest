package uz.mu.autotest.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.mu.autotest.processor.WorkflowResultProcessor;
import uz.mu.autotest.processor.WorkflowResultsPayload;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WorkflowController {

    private final WorkflowResultProcessor workflowResultProcessor;

    @PostMapping("/workflow/results")
    public void workflowResult(@RequestBody WorkflowResultsPayload resultsPayload, HttpServletRequest request) {
        log.info("Received workflow results: {}", resultsPayload);
        logRequestDetails(request);
        workflowResultProcessor.process(resultsPayload);
    }
    private void logRequestDetails(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String remoteHost = request.getRemoteHost();
        String remotePort = String.valueOf(request.getRemotePort());
        String userAgent = request.getHeader("User-Agent");

        log.info("Request received from:");
        log.info("IP Address: {}", remoteAddr);
        log.info("Host: {}", remoteHost);
        log.info("Port: {}", remotePort);
        log.info("User-Agent: {}", userAgent);
    }
}
