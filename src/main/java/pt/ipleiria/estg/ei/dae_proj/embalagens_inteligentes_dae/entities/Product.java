package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name= "getAllProducts", query= "SELECT p FROM Product p ORDER BY p.id DESC"),
})
@Table(
        name="products",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
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

    public Product() {
    }

    public Product(String name, String description, ProductManufacturer manufacturer) {
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
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

}