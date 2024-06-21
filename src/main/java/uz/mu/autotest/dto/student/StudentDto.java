package uz.mu.autotest.dto.student;

import uz.mu.autotest.dto.group.GroupDto;

import java.util.List;

public record StudentDto(Long id, String firstName, String lastName, String username, List<GroupDto> groups) {
}
