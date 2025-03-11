package ge.temo.carengine.cars.user.persistance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role", schema = "cars")
@Getter
@Setter
public class Role {
    @Id
    private Long id;

    @Column
    private String name;
}
