package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
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

    public void create(String name, String description, ProductManufacturer productManufacturer) throws MyEntityExistsException {
        var product = new Product(name, description, productManufacturer);
        entityManager.persist(product);
    }

    public List<Product> getAll() {
        return entityManager.createNamedQuery("getAllProducts", Product.class).getResultList();
    }

    public void update(long id, String name, String description, ProductManufacturer productManufacturer) throws MyEntityNotFoundException {

        if (!exists(id)) {
            throw new MyEntityNotFoundException("Student with id: " + id + " not found");
        }

        Product product = entityManager.find(Product.class, id);
        entityManager.lock(product, LockModeType.OPTIMISTIC);

        product.setName(name);
        product.setDescription(description);
        product.setProductManufacturer(productManufacturer);

        /*
        if (product.getProductManufacturer().getUsername() != productManufacturer.getUsername()) {
            Course course = entityManager.find(Course.class, courseCode);
            if (course == null) {
                throw new MyEntityNotFoundException("Course with code: " + courseCode + " not found"); //lança a excecao
            }
            student.setCourse(course);
        }
        */

    }

    public void delete(long id) throws MyEntityNotFoundException {

        Product product = entityManager.find(Product.class, id);
        if (product == null) {
            throw new MyEntityNotFoundException("Product with id: " + id + " not found");
        }

        entityManager.remove(product);
    }



}
