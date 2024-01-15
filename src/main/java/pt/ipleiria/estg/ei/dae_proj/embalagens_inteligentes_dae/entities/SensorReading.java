package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@NamedQueries(
        @NamedQuery(name = "getAllSensorReadings", query = "SELECT sr FROM SensorReading sr")
)
@Table(name="sensor_readings")
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date date;

    @NotNull
    private float value;

    @OneToOne
    private Sensor sensor;

    boolean violatesQualityConstraint = false;

    public SensorReading(){
    }

    public SensorReading(float value, Sensor sensor) {
        this.date = new Date();
        this.value = value;
        this.sensor = sensor;
    }

    public Date getDate() {
        return date;
    }

    public float getValue() {
        return value;
    }

    public Sensor getSensor() {
        return this.sensor;
    }

    public boolean doesViolateQualityConstraint() {
        return violatesQualityConstraint;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setViolatesQualityConstraint(boolean violates) {
        this.violatesQualityConstraint = violates;
    }
}