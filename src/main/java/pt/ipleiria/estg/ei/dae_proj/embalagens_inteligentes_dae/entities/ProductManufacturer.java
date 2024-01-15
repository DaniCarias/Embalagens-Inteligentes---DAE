package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;

@Entity
@NamedQueries({
        @NamedQuery(name= "getAllProductManufacturer", query= "SELECT p FROM ProductManufacturer p ORDER BY p.username DESC"),
})
public class ProductManufacturer extends User{
    @OneToMany
    private List<Product> products;

    public ProductManufacturer() {

    }

    public ProductManufacturer(String username, String name, String password, String address, int phoneNumber) {
        super(username, name, password, address, phoneNumber);
        products = new LinkedList<>();
    }





}
