package uz.mu.autotest.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Attempt {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long runNumber;
    private String detailsUrl;
    @Enumerated(EnumType.STRING)
    private Result result;
    @ManyToOne(cascade = CascadeType.ALL)
    private UserTakenLab userTakenLab;

    @Override
    public String toString() {
        return "Attempt{" +
                "id=" + id +
                ", runNumber=" + runNumber +
                ", detailsUrl='" + detailsUrl + '\'' +
                ", result=" + result +
                '}';
    }
}
