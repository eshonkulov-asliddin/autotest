package uz.mu.autotest.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Lab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lab_name")
    private String labName;

    @Column(name = "github_url", unique = true)
    private String githubUrl;

    @Enumerated(EnumType.STRING)
    private LabStatus status;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "course_id")
    private Course course;

    @Override
    public String toString() {
        return "Lab{" +
                "id=" + id +
                ", labName='" + labName + '\'' +
                ", githubUrl='" + githubUrl + '\'' +
                ", status=" + status +
                ", course=" + course.getName() +
                '}';
    }
}
