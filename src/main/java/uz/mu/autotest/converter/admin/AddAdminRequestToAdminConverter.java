package uz.mu.autotest.converter.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uz.mu.autotest.dto.admin.AddAdminRequest;
import uz.mu.autotest.model.Admin;

@Component
@RequiredArgsConstructor
public class AddAdminRequestToAdminConverter implements Converter<AddAdminRequest, Admin> {
    @Override
    public Admin convert(AddAdminRequest source) {
        Admin admin = new Admin();
        admin.setFirstName(source.firstName());
        admin.setLastName(source.lastName());
        return admin ;
    }
}
