package uz.mu.autotest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`ADMIN`")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {
}
