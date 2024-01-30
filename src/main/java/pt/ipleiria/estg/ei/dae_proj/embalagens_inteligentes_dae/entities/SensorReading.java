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
        @NamedQuery(name = "getAllSensorReadings", query= "SELECT s FROM SensorReading s ORDER BY s.sensor.id DESC"),
        @NamedQuery(name = "getAllSensorReadingsForSensor", query = "SELECT sr FROM SensorReading sr WHERE sr.sensor.id = :sensorId"),
        @NamedQuery(name = "getViolatingReadingsForProduct", query = "SELECT sr FROM SensorReading sr " +
                "INNER JOIN QualityConstraint qc ON sr.sensor.id = qc.sensor.id " +
                "INNER JOIN Product p ON qc.product.id = p.id " +
                "WHERE sr.violatesQualityConstraint = true AND p.id = :productId"),
        @NamedQuery(name = "getReadingsForProduct", query = "SELECT sr FROM SensorReading sr " +
                "INNER JOIN Sensor s ON s.id = sr.sensor.id " +
                "INNER JOIN Package p ON p.id = s._package.id " +
                "WHERE p.product.id = :productId"),
        @NamedQuery(name = "getAllSensorReadingsForPackage", query = "SELECT sr FROM SensorReading sr " +
                "JOIN Sensor s ON sr.sensor.id = s.id " +
                "JOIN Package p ON s._package.id = p.id " +
                "WHERE p.id = :packageId"),
})
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date date;

    @NotNull
    private float value;

    @ManyToOne
    private Sensor sensor;

    boolean violatesQualityConstraint = false;

    public SensorReading(){
    }

    public SensorReading(float value, Sensor sensor) {
        this.date = new Date();
        this.value = value;
        this.sensor = sensor;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public float getValue() {
        return value;
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
    
    public Sensor getSensor() {
        return sensor;
    }

    public void setViolatesQualityConstraint(boolean violates) {
        this.violatesQualityConstraint = violates;
    }
}