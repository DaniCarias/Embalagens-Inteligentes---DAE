package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.ProductManufacturer;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class ProductManufacturerBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(String username) {
        return entityManager.find(ProductManufacturer.class, username) != null;
    }

    public ProductManufacturer create(String username, String password, String name, String address, int phoneNumber) throws MyEntityExistsException {
        if (exists(username)) {
            throw new MyEntityExistsException("Product Manufacturer with username: " + username + " already exists");
        }
        var productManufacturer = new ProductManufacturer(username, password, name, address, phoneNumber);
        entityManager.persist(productManufacturer);
        return productManufacturer;
    }

    public List<ProductManufacturer> getAll() {
        return entityManager.createNamedQuery("getAllProductManufacturer", ProductManufacturer.class).getResultList();
    }

    public ProductManufacturer getProductManufacturer(String username) throws MyEntityNotFoundException {
        ProductManufacturer productManufacturer = entityManager.find(ProductManufacturer.class, username);
        if (productManufacturer == null)
            throw new MyEntityNotFoundException("Product Manufacturer with id: " + username + " not found");
        return productManufacturer;
    }

    public void update(String username, String password, String name, String address, int phoneNumber) {
        ProductManufacturer productManufacturer = entityManager.find(ProductManufacturer.class, username);
        if (productManufacturer != null) {
            productManufacturer.setPassword(password);
            productManufacturer.setName(name);
            productManufacturer.setAddress(address);
            productManufacturer.setPhoneNumber(phoneNumber);
            entityManager.merge(productManufacturer);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {

        ProductManufacturer productManufacturer = entityManager.find(ProductManufacturer.class, username);
        if (productManufacturer == null)
            throw new MyEntityNotFoundException("Product Manufacturer with id: " + username + " not found");

        entityManager.lock(productManufacturer, LockModeType.OPTIMISTIC);
        entityManager.remove(productManufacturer);
    }

}
