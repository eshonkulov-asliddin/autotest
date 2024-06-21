package uz.mu.autotest.converter.group;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.group.AddGroupRequest;
import uz.mu.autotest.model.Group;

@Component
@RequiredArgsConstructor
public class AddGroupRequestToGroupConverter implements Converter<AddGroupRequest, Group> {

    @Override
    public Group convert(AddGroupRequest source) {
        Group group = new Group();
        group.setName(source.groupName());
        return group;
    }
}
