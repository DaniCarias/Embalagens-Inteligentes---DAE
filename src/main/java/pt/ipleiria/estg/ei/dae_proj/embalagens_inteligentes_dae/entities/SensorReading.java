package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(
        name="sensor_readings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date date;

    @NotNull
    private float value;

    @OneToOne
    private Sensor sensor;

    public SensorReading(){
    }

    public SensorReading(float value) {
        this.date = new Date();
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public float getValue() {
        return value;
    }
}