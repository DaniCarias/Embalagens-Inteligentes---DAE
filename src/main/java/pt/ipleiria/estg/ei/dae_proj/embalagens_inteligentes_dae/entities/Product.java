package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import com.sun.istack.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name= "getAllProducts", query= "SELECT p FROM Product p ORDER BY p.id DESC"),
        @NamedQuery(name= "getAllProductsProductManufacturer", query= "SELECT p FROM Product p WHERE p.manufacturer.username = :username ORDER BY p.id DESC"),
        @NamedQuery(name= "getProductsByEndConsumer", query= "SELECT p FROM Product p " +
                "INNER JOIN Order ord ON ord.endConsumer.username LIKE :username " +
                "INNER JOIN Package pck ON pck.order.id = ord.id " +
                "WHERE p._package.id = pck.id " +
                "ORDER BY p.id DESC"),
        @NamedQuery(name= "getProductsForEndConsumer", query= "SELECT p FROM Product p "+
                "INNER JOIN Package pck ON pck.id = p._package.id " +
                "WHERE p._package.id != null AND pck.order.id = NULL"),
        @NamedQuery(name= "getProductsForEndConsumerById", query= "SELECT p FROM Product p "+
                "INNER JOIN Package pck ON pck.id = p._package.id " +
                "WHERE p._package.id != null AND pck.order.id = NULL AND p.id = :id"),
})
@Table(
        name="products",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
@SQLDelete(sql="UPDATE products SET deleted = true WHERE id = ? AND version = ?")
@Where(clause = "deleted = false")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    @ManyToOne
    private ProductManufacturer manufacturer;
    @OneToOne
    private Package _package;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<QualityConstraint> qualityConstraints;

    @Version
    private int version;
    private Date deleted_at;
    private boolean deleted = Boolean.FALSE;

    public Product() {
        this.qualityConstraints = new LinkedList<>();
    }

    public Product(String name, String description, ProductManufacturer manufacturer) {
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.qualityConstraints = new LinkedList<>();
        this._package = null;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ProductManufacturer getProductManufacturer() {
        return manufacturer;
    }
    public void setProductManufacturer(ProductManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
    public Package getPackage() {
        return _package;
    }
    public void setPackage(Package _package) {
        this._package = _package;
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

    public List<QualityConstraint> getQualityConstraints() {
        return qualityConstraints;
    }

    public void addQualityConstraint(QualityConstraint constraint) {
        this.qualityConstraints.add(constraint);
    }

}