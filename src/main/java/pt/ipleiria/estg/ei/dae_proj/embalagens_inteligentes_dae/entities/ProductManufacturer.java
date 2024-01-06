package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class ProductManufacturer extends User{
    @OneToMany
    private List<Product> products;

    public ProductManufacturer() {

    }

    public ProductManufacturer(String username, String name, String password, String address, int phoneNumber) {
        super(username, name, password, address, phoneNumber);
    }





}
