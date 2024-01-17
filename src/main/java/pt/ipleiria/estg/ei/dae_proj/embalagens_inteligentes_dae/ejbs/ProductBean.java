package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import com.sun.istack.Nullable;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;

import java.util.Date;
import java.util.List;

@Stateless
public class ProductBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long id) {
        Query query = entityManager.createQuery("SELECT COUNT(p.id) FROM Product p WHERE p.id = :id", Long.class);
        query.setParameter("id", id);
        return (Long)query.getSingleResult() > 0L;
    }

    public Product find(long id) {
        return entityManager.find(Product.class, id);
    }

    public boolean productManufacturer_verify(ProductManufacturer prodManufacture) {
        ProductManufacturer prod_Manufacture = entityManager.find(ProductManufacturer.class, prodManufacture.getUsername());
        return prod_Manufacture != null ? true : false;
    }

    public Product create(String name, String description, ProductManufacturer productManufacturer) throws MyEntityNotFoundException {

        if (!productManufacturer_verify(productManufacturer))
            throw new MyEntityNotFoundException("Product Manufacturer with username: " + productManufacturer.getUsername() + " not found");

        var product = new Product(name, description, productManufacturer);

        entityManager.persist(product);

        return product;
    }

    public List<Product> getAll() {
        return entityManager.createNamedQuery("getAllProducts", Product.class).getResultList();
    }

    public Product getProduct(long id) throws MyEntityNotFoundException {
        Product product = entityManager.find(Product.class, id);
        if (product == null)
            throw new MyEntityNotFoundException("Product with id: " + id + " not found");
        return product;
    }

    public void update(long id, String name, String description, ProductManufacturer productManufacturer, long package_id) throws MyEntityNotFoundException {

        if (!exists(id))
            throw new MyEntityNotFoundException("Product with id: " + id + " not found");

        if (!productManufacturer_verify(productManufacturer))
            throw new MyEntityNotFoundException("Product Manufacturer with username: " + productManufacturer.getUsername() + " not found");

        Product product = entityManager.find(Product.class, id);
        entityManager.lock(product, LockModeType.OPTIMISTIC);

        product.setName(name);
        product.setDescription(description);
        product.setProductManufacturer(productManufacturer);
        product.setPackage(entityManager.find(Package.class, package_id));
    }

    public boolean delete(long id) throws MyEntityNotFoundException {

        Product product = entityManager.find(Product.class, id);
        if (product == null)
            throw new MyEntityNotFoundException("Product with id: " + id + " not found");

        product.setDeleted_at(new Date());
        entityManager.persist(product);
        entityManager.flush();

        entityManager.lock(product, LockModeType.OPTIMISTIC);
        entityManager.remove(product);
        return true;
    }

    public void addPackage(long product_id, long package_id) throws MyEntityNotFoundException {

        if (!exists(product_id))
            throw new MyEntityNotFoundException("Product with id: " + product_id + " not found");

        Package _package = entityManager.find(Package.class, package_id);
        Product product = entityManager.find(Product.class, product_id);

        if(product.getPackage() != null)
            throw new MyEntityNotFoundException("Product with id: " + product_id + " has already a package");

        entityManager.lock(product, LockModeType.OPTIMISTIC);

        product.setPackage(_package);
        entityManager.merge(product);
    }

    public void removePackage(long product_id) throws MyEntityNotFoundException {

        if (!exists(product_id))
            throw new MyEntityNotFoundException("Product with id: " + product_id + " not found");

        Product product = entityManager.find(Product.class, product_id);

        if(product.getPackage() == null)
            throw new MyEntityNotFoundException("Product with id: " + product_id + " has no package");

        entityManager.lock(product, LockModeType.OPTIMISTIC);

        product.setPackage(null);
        entityManager.merge(product);
    }

}
