package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

@Stateless
//Qualquer funcao acedida pelo cliente não guarda estado e pode ser chamada o numero de vezes que queira sem problema
public class ProductBean {

    @PersistenceContext //vai buscar o contexto da BD
    private EntityManager entityManager;

    public boolean exists(String username) {
        Query query = entityManager.createQuery("SELECT COUNT(p.username) FROM Product p WHERE p.username = :username", Long.class);
        query.setParameter("username", username);
        return (Long)query.getSingleResult() > 0L;
    }

    public void create(/* DATA TO CREATE PRDODUCT */) throws MyEntityExistsException, MyEntityNotFoundException { //indica que vai lançar uma excecao

        if (exists(username)) {
            throw new MyEntityExistsException("Product with username: " + username + " already exists"); //lança a excecao
        }
        /*
        Course course = entityManager.find(Course.class, courseCode);
        if (course == null) {
            throw new MyEntityNotFoundException("Course with code: " + courseCode + " dont exists"); //lança a excecao
        }*/

        var product = new Product(/* DATA TO CREATE PRDODUCT */);
        //course.addStudent(student);
        entityManager.persist(product);
    }

    public List<Product> getAll() { //metodo que cria a query e vai buscar o resultado a BD e da return do mesmo
        return entityManager.createNamedQuery("getAllProducts", Product.class).getResultList();
    }

    public Product find(String username) {
        return entityManager.find(Product.class, username);
    }

    public void update(/* DATA TO CREATE PRDODUCT */) throws MyEntityNotFoundException {

        if (!exists(username)) {
            throw new MyEntityNotFoundException("Product with username: " + username + " not found"); //lança a excecao
        }

        /*Course course = entityManager.find(Course.class, courseCode);
        if (course == null) {
            throw new MyEntityNotFoundException("Course with code: " + courseCode + " dont exists"); //lança a excecao
        }*/

        var product = new Product(/* DATA TO CREATE PRDODUCT */);
        //course.addStudent(student);
        entityManager.persist(product);

    }

    public void delete(String username) throws MyEntityNotFoundException {

        Product product = find(username);
        if (product == null) {
            throw new MyEntityNotFoundException("Product with username: " + username + " not found"); //lança a excecao
        }

        entityManager.remove(product);
    }



}
