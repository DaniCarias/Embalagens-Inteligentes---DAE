package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name= "getAllPackages", query= "SELECT p FROM Package p ORDER BY p.id DESC"),
})
@Table(
        name="packages",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
public class Package {

    enum PackageType {
        PRIMARIA,
        SECUNDARIA,
        TERCIARIA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private PackageType packageType;

    /*
        Last time the package was opened, to detect unauthorized access.
    */
    private Date lastTimeOpened;
    private String materialEmbalagem;
    @OneToOne
    private Product product;
    @ManyToOne
    private Order order;
    @OneToMany
    private List<Sensor> sensors;
    @Version
    private int version;
    public Package() {
        this.sensors = new LinkedList<>();
    }

    public Package(long id, PackageType packageType, Date lastTimeOpened, String materialEmbalagem, Product product) {
        this.id = id;
        this.packageType = packageType;
        this.lastTimeOpened = lastTimeOpened;
        this.materialEmbalagem = materialEmbalagem;
        this.product = product;
        this.sensors = new LinkedList<>();
    }

    public void addSensor(Sensor sensor) {
        this.sensors.add(sensor);
    }
    public void removeSensor(Sensor sensor) {
        this.sensors.remove(sensor);
    }


}
