package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(name= "getAllSensorsDefault", query= "SELECT s FROM SensorsDefault s ORDER BY s.id DESC"),
})
@Table(
        name="sensors_default",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
public class SensorsDefault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;


    public SensorsDefault() {
    }

    public SensorsDefault(String name) {
        this.name = name;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
