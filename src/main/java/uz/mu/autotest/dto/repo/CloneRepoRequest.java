package uz.mu.autotest.dto.repo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CloneRepoRequest(
        String owner,
        @JsonProperty("name")
        String labName,
        String description,
        @JsonProperty("include_all_branches")
        boolean doIncludeAllBranch,
        @JsonProperty("private")
        boolean isPrivate ) { }
