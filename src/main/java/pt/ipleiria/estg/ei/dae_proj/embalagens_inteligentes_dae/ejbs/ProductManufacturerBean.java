package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Product;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.ProductManufacturer;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Hasher;

import java.util.List;

@Stateless
public class ProductManufacturerBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;

    public boolean exists(String username) {
        return entityManager.find(ProductManufacturer.class, username) != null;
    }

    public ProductManufacturer find(String username) {
        return entityManager.find(ProductManufacturer.class, username);
    }

    public ProductManufacturer create(String username, String password, String name, String address, int phoneNumber) throws MyEntityExistsException {
        if (exists(username)) {
            throw new MyEntityExistsException("Product Manufacturer with username: " + username + " already exists");
        }
        var productManufacturer = new ProductManufacturer(username, name, hasher.hash(password), address, phoneNumber);
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

    public boolean delete(String username) throws MyEntityNotFoundException {

        ProductManufacturer productManufacturer = entityManager.find(ProductManufacturer.class, username);
        if (productManufacturer == null)
            throw new MyEntityNotFoundException("Product Manufacturer with id: " + username + " not found");

        entityManager.lock(productManufacturer, LockModeType.OPTIMISTIC);
        entityManager.remove(productManufacturer);
        return true
    }

    public void addProduct(String username, long product_id) throws MyEntityNotFoundException {
        ProductManufacturer productManufacturer = entityManager.find(ProductManufacturer.class, username);
        Product product = entityManager.find(Product.class, product_id);

        if (productManufacturer == null)
            throw new MyEntityNotFoundException("Product Manufacturer with id: " + username + " not found");
        if (product == null)
            throw new MyEntityNotFoundException("Product with id: " + product_id + " not found");

        entityManager.lock(productManufacturer, LockModeType.OPTIMISTIC);

        productManufacturer.addProduct(product);
        entityManager.merge(productManufacturer);
    }

    public void removeProduct(String username, long product_id) throws MyEntityNotFoundException {
        ProductManufacturer productManufacturer = entityManager.find(ProductManufacturer.class, username);
        Product product = entityManager.find(Product.class, product_id);

        if (productManufacturer == null)
            throw new MyEntityNotFoundException("Product Manufacturer with id: " + username + " not found");
        if (product == null)
            throw new MyEntityNotFoundException("Product with id: " + product_id + " not found");

        entityManager.lock(productManufacturer, LockModeType.OPTIMISTIC);

        productManufacturer.removeProduct(product);
        entityManager.merge(productManufacturer);
    }


}
