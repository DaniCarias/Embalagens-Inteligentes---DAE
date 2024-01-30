package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name= "getAllOrders", query= "SELECT o FROM Order o ORDER BY o.id DESC"),
        @NamedQuery(name= "getOrdersByEndConsumer", query= "SELECT o FROM Order o WHERE o.endConsumer.username = :username ORDER BY o.id DESC"),
        @NamedQuery(name= "getOrdersByIdByEndConsumer", query= "SELECT o FROM Order o WHERE o.endConsumer.username = :username AND o.id = :id ORDER BY o.id DESC"),
})
@Table(
        name="orders",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
@SQLDelete(sql="UPDATE orders SET deleted = true WHERE id = ? AND version = ?")
@Where(clause = "deleted = false")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany(mappedBy = "order")
    private List<Package> packages;
    @ManyToOne
    private EndConsumer endConsumer;
    @Version
    private int version;
    private Date deleted_at;
    private boolean deleted = Boolean.FALSE;


    public Order() {
        this.packages = new LinkedList<Package>();
    }

    public Order(EndConsumer endConsumer) {
        this.packages = new LinkedList<Package>();
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
    public List<Package> getPackages() {
        return packages;
    }
    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }
    public Date getDeleted_at() {
        return deleted_at;
    }
    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public void addPackage(Package _package){
        if(!packages.contains(_package))
            this.packages.add(_package);
    }
    public void removePackage(Package _package){
        if(packages.contains(_package))
            this.packages.remove(_package);
    }
}
