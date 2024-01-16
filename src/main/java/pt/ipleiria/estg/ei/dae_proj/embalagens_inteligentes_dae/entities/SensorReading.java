package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(
        name="sensor_readings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
@NamedQueries({
        @NamedQuery(name= "getAllSensorReading", query= "SELECT s FROM SensorReading s ORDER BY s.sensor.id DESC"),
})
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
    public void setValue(float value) {
        this.value = value;
    }
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
    public Sensor getSensor() {
        return sensor;
    }
}