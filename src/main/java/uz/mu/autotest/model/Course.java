package uz.mu.autotest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "course")
    private Set<Lab> labs;

    @ManyToMany(mappedBy = "courses")
    private Set<Group> groups;

    @ManyToOne
    private Teacher teacher;

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + name + '\'' +
                ", labsCount=" + labs.size() +
                '}';
    }
}
