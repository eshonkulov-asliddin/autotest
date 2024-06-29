package uz.mu.autotest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Teacher extends User {

    @OneToMany(mappedBy = "teacher")
    private Set<Course> courses;

}
