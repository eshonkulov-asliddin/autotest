package uz.mu.autotest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class UserTakenLab {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String githubUrl;
    @Enumerated(EnumType.STRING)
    private LabStatus status;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Lab lab;
    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return "UserTakenLab{" +
                "id=" + id +
                ", githubUrl='" + githubUrl + '\'' +
                ", status=" + status +
                ", course=" + course.getName() +
                ", lab=" + lab.getLabName() +
                ", user=" + user.getUsername() +
                '}';
    }
}
