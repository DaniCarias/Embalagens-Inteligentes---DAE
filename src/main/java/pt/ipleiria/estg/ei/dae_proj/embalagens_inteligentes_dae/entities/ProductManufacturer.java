package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws.PackageService;

@Entity
@NamedQueries({
        @NamedQuery(name= "getAllProductManufacturer", query= "SELECT p FROM ProductManufacturer p ORDER BY p.username DESC"),
})
public class ProductManufacturer extends User{
    @OneToMany(mappedBy = "manufacturer")
    private List<Product> products;

    @OneToMany(mappedBy = "manufacturer")
    private List<Package> packages;


    public ProductManufacturer() {
        products = new LinkedList<>();
        packages = new LinkedList<>();
    }

    public ProductManufacturer(String username, String name, String password, String address, int phoneNumber) {
        super(username, name, password, address, phoneNumber);
        products = new LinkedList<>();
        packages = new LinkedList<>();
    }


    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }


    public void addProduct(Product product) {
        if(!products.contains(product)) {
            this.products.add(product);
        }
    }

    public void removeProduct(Product product) {
        if(products.contains(product)) {
            this.products.remove(product);
        }
    }


    public void addPackage(Package _package) {
        if(!packages.contains(_package)) {
            this.packages.add(_package);
        }
    }

    public void removePackage(Package _package) {
        if(packages.contains(_package)) {
            this.packages.remove(_package);
        }
    }

}
