package uz.mu.autotest.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class GroupLabAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Group group;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Lab lab;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Course course;

    private LocalDateTime deadline;
}
