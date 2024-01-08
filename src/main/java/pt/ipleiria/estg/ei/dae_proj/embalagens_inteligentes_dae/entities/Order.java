package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name= "getAllOrders", query= "SELECT o FROM Order o ORDER BY o.id DESC"),
})
@Table(
        name="orders",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<Package> packages;
    @ManyToOne
    private EndConsumer endConsumer;
    @Version
    private int version;

    public Order() {
        this.packages = new LinkedList<>();
    }

    public Order(EndConsumer endConsumer) {
        this.packages = new LinkedList<>();
        this.endConsumer = endConsumer;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public List<Package> getPackage() {
        return packages;
    }
    public void setPackage(List<Package> packages) {
        this.packages = packages;
    }
    public EndConsumer getEndConsumer() {
        return endConsumer;
    }
    public void setEndConsumer(EndConsumer endConsumer) {
        this.endConsumer = endConsumer;
    }


    public void addPackage(Package _package){
        this.packages.add(_package);
    }
    public void removePackage(Package _package){
        this.packages.remove(_package);
    }
}
