package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.enterprise.inject.Typed;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Type;
import org.hibernate.mapping.Value;
import org.hibernate.usertype.UserType;

import java.util.LinkedList;
import java.util.List;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name= "getAllSensores", query= "SELECT s FROM Sensor s ORDER BY s.name DESC"),
})
@Table(
        name="sensors",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany
    private List<SensorReading> readings;

    @Version
    private int version;

    public Sensor() {
        this.readings = new LinkedList<>();
    }

    public Sensor(String name){
        this.name = name;
        this.readings = new LinkedList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<SensorReading> getReadings() {
        return readings;
    }
    public void addReading(float value) {
        this.readings.add(new SensorReading(value));
    }
}
