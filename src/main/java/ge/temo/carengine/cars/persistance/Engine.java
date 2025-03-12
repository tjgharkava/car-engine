package ge.temo.carengine.cars.persistance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "engine")
@SequenceGenerator(name = "engine_seq_gen", sequenceName = "engine_seq", allocationSize = 1)
@Getter
@Setter
public class Engine {
    @Id
    @GeneratedValue(generator = "engine_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "horse_power")
    private int horsePower;

    @Column(name = "capacity")
    private double capacity;
}
