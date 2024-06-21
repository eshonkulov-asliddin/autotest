package uz.mu.autotest.dto.group;

import jakarta.validation.constraints.NotEmpty;

public record AddGroupRequest(@NotEmpty String groupName) {
}
