package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.List;

public class PackageBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long id) {
        Query query = entityManager.createQuery("SELECT COUNT(p.id) FROM Package p WHERE p.id = :id", Long.class);
        query.setParameter("id", id);
        return (Long)query.getSingleResult() > 0L;
    }

    public Package find(long id) {
        return entityManager.find(Package.class, id);
    }

    public boolean product_verify(Product product) {
        Product prod = entityManager.find(Product.class, product.getId());
        return prod != null ? true : false;
    }


    //Confirmar  se pode ser public o packageType para usar no create !!!!!!!!!!!!!!!!!!!!
    public Package create(Package.PackageType type, Date lastTimeOpened, String material, Product product) throws MyEntityNotFoundException {

        if (!product_verify(product))
            throw new MyEntityNotFoundException("Product with id: " + product.getId() + " not found");

        var new_package = new Package(type, lastTimeOpened, material, product);
        entityManager.persist(new_package);
        return new_package;
    }

    public List<Package> getAll() {
        return entityManager.createNamedQuery("getAllPackages", Package.class).getResultList();
    }

    public void update(long id, Package.PackageType type, Date lastTimeOpened, String material, Product product) throws MyEntityNotFoundException {

        if (!exists(id)) {
            throw new MyEntityNotFoundException("Package with id: " + id + " not found");
        }

        if (!product_verify(product))
            throw new MyEntityNotFoundException("Product with id: " + product.getId() + " not found");

        Package _package = entityManager.find(Package.class, id);
        entityManager.lock(_package, LockModeType.OPTIMISTIC);

        _package.setPackageType(type);
        _package.setLastTimeOpened(lastTimeOpened);
        _package.setMaterial(material);
        _package.setProduct(product);
    }

    public void delete(long id) throws MyEntityNotFoundException {

        Package _package = entityManager.find(Package.class, id);
        if (_package == null) {
            throw new MyEntityNotFoundException("Package with id: " + id + " not found");
        }

        //entityMannager.lock(_package, LockModeType.OPTIMISTIC); ???????????
        entityManager.remove(_package);
    }


}
