package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/*
    As armas e os barões assinalados,
    Que da ocidental praia Lusitana,
    Por mares nunca de antes navegados,
    Passaram ainda além da Taprobana,
    Em perigos e guerras esforçados,
    Mais do que prometia a força humana,
    E entre gente remota edificaram
    Novo Reino, que tanto sublimaram;
 */

@Entity
@NamedQueries({
        @NamedQuery(name="getAllQualityConstraints", query="SELECT q FROM QualityConstraint q"),
        @NamedQuery(name="getAllQualityConstraintsForProduct", query="SELECT q FROM QualityConstraint q WHERE q.product.id = :productId"),
        @NamedQuery(name="getAllQualityConstraintsForSensor", query="SELECT q FROM QualityConstraint q WHERE q.sensor.id = :sensorId"),
        @NamedQuery(name="getAllQualityConstraintsForOrder", query="SELECT q FROM QualityConstraint q JOIN Product p " +
                "ON q.product.id = p.id JOIN Package pack ON p.id = pack.product.id JOIN Order o ON pack.order.id = o.id " +
                "WHERE o.id = :orderId"),
        @NamedQuery(name="getAllQualityConstraintsForPackage", query="SELECT q FROM QualityConstraint q JOIN Product p " +
                "ON q.product.id = p.id JOIN Package pack ON p.id = pack.product.id WHERE pack.id = :packageId")
})
@Table(
        name = "quality_constraints",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
public class QualityConstraint {

    public enum ConstraintType {
        LOWER,
        UPPER,
        EXACT
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private float value;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ConstraintType type;

    @ManyToOne
    private Product product; // the product imposing this constraint

    @ManyToOne
    private Sensor sensor; // the sensor responsible by measuring this constraint

    public QualityConstraint() {

    }

    public QualityConstraint(float value, ConstraintType type, Sensor sensor, Product product) {
        this.value = value;
        this.type = type;
        this.sensor = sensor;
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public ConstraintType getType() {
        return type;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Product getProduct() {
        return product;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setType(ConstraintType type) {
        this.type = type;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
