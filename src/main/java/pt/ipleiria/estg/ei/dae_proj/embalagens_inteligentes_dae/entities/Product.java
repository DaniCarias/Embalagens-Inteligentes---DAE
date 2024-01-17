package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import com.sun.istack.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name= "getAllProducts", query= "SELECT p FROM Product p WHERE p.deleted = false ORDER BY p.id DESC"),
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
    @Version
    private int version;
    private Date deleted_at;
    private boolean deleted = Boolean.FALSE;

    public Product() {
    }

    public Product(String name, String description, ProductManufacturer manufacturer) {
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
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


}