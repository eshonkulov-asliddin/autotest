package uz.mu.autotest.converter.group;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.group.GroupDto;
import uz.mu.autotest.model.Group;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GroupToGroupDtoConverter implements Converter<Group, GroupDto> {
    @Override
    public GroupDto convert(Group source) {
        return new GroupDto(source.getId(), source.getName());
    }

    public List<GroupDto> convertGroupsToGroupDtos(Set<Group> groups) {
        if (groups == null || groups.isEmpty()) {
            return Collections.emptyList();
        }

        return groups.stream()
                .map(this::convert)
                .toList();
    }

}
