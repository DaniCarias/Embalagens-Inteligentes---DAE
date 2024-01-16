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

    //Confirmar se pode ser publico para o create no bean do package
    public enum PackageType {
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
    private String material;
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

    public Package(PackageType packageType, Date lastTimeOpened, String material, Product product) {
        this.packageType = packageType;
        this.lastTimeOpened = lastTimeOpened;
        this.material = material;
        this.product = product;
        this.sensors = new LinkedList<>();
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public PackageType getPackageType() {
        return packageType;
    }
    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }
    public Date getLastTimeOpened() {
        return lastTimeOpened;
    }
    public void setLastTimeOpened(Date lastTimeOpened) {
        this.lastTimeOpened = lastTimeOpened;
    }
    public String getMaterial() {
        return material;
    }
    public void setMaterial(String material) {
        this.material = material;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public List<Sensor> getSensors() {
        return sensors;
    }
    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }


    public void addSensor(Sensor sensor) {
        this.sensors.add(sensor);
    }
    public void removeSensor(Sensor sensor) {
        this.sensors.remove(sensor);
    }


}
