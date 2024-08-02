package uz.mu.autotest.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student extends User {

    private String login;

    @ManyToMany(mappedBy = "students", cascade= {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Group> groups = new HashSet<>();

}
