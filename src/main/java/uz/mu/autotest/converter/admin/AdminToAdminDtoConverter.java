package uz.mu.autotest.converter.admin;

import org.springframework.core.convert.converter.Converter;
import uz.mu.autotest.dto.admin.AdminDto;
import uz.mu.autotest.model.Admin;

public class AdminToAdminDtoConverter implements Converter<Admin, AdminDto> {
    @Override
    public AdminDto convert(Admin source) {
        return new AdminDto(source.getId(), source.getFirstName(), source.getLastName(), source.getUsername());
    }
}
