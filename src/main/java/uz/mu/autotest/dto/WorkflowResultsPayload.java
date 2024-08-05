package uz.mu.autotest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkflowResultsPayload {
    private String status;
    private Repository repository;
    private String branch;
    private String commit;
    private long runId;
    private long runNumber;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Repository {
        private String name;
        private String owner;
    }
}
