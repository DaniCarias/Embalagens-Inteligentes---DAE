package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Stateless
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

    public Package create(Package.PackageType type, Date lastTimeOpened, String material) throws MyEntityNotFoundException {

        var new_package = new Package(type, lastTimeOpened, material);
        entityManager.persist(new_package);
        return new_package;
    }

    public List<Package> getAll() {
        return entityManager.createNamedQuery("getAllPackages", Package.class).getResultList();
    }

    public void update(long id, Package.PackageType type, Date lastTimeOpened, String material/*, Product product*/) throws MyEntityNotFoundException {

        if (!exists(id)) {
            throw new MyEntityNotFoundException("Package with id: " + id + " not found");
        }

        /*if (!product_verify(product))
            throw new MyEntityNotFoundException("Product with id: " + product.getId() + " not found");*/

        Package _package = entityManager.find(Package.class, id);
        entityManager.lock(_package, LockModeType.OPTIMISTIC);

        _package.setPackageType(type);
        _package.setLastTimeOpened(lastTimeOpened);
        _package.setMaterial(material);
        //_package.setProduct(product);
    }

    public boolean delete(long id) throws MyEntityNotFoundException {

        Package _package = entityManager.find(Package.class, id);
        if (_package == null)
            throw new MyEntityNotFoundException("Package with id: " + id + " not found");

        _package.setDeleted_at(new Date());
        entityManager.persist(_package);
        entityManager.flush();

        entityManager.lock(_package, LockModeType.OPTIMISTIC);
        entityManager.remove(_package);
        return true;
    }

    public void addOrder(long id, long order_id) throws MyEntityNotFoundException, MyEntityExistsException {

        Package _package = entityManager.find(Package.class, id);
        Order order = entityManager.find(Order.class, order_id);

        if (_package == null)
            throw new MyEntityNotFoundException("Package with id: " + id + " not found");

        if (order == null)
            throw new MyEntityNotFoundException("Order with id: " + order_id + " not found");

        if (_package.getOrder() != null)
            throw new MyEntityExistsException("Package with id: " + id + " already has an order");

        entityManager.lock(order, LockModeType.OPTIMISTIC);
        order.addPackage(_package);

        entityManager.lock(_package, LockModeType.OPTIMISTIC);
        _package.setOrder(order);
    }


    public void removeAllPackages(Order order) throws MyEntityNotFoundException {

        for (var pck : order.getPackage()){
            removeOrder(pck.getId());
        }

    }

    public void removeOrder(long id) throws MyEntityNotFoundException {

        Package _package = entityManager.find(Package.class, id);

        if (_package == null)
            throw new MyEntityNotFoundException("Package with id: " + id + " not found");

        Order order = _package.getOrder();
        if (order == null)
            throw new MyEntityNotFoundException("Package with id: " + id + " does not have a order");

        entityManager.lock(order, LockModeType.OPTIMISTIC);
        order.removePackage(_package);

        entityManager.lock(_package, LockModeType.OPTIMISTIC);
        _package.setOrder(null);
    }


    public Package getPackageSensors(long id) throws MyEntityNotFoundException{
        Package _package = entityManager.find(Package.class, id);
        if(!exists(_package.getId())){
            throw new MyEntityNotFoundException("Package with id: " + id + " not found");
        }
        Hibernate.initialize(_package.getSensors());
        return _package;
    }

    public List<Package> getPackagesByProducts(List<Product> products){

        List<Package> packages = new LinkedList<>();

        for (Product product : products) {
            if(product.getPackage() == null)
                continue;

            var package_id = product.getPackage().getId();
            Package _package = find(package_id);
            packages.add(_package);
        }

        return packages;
    }

    public void addProduct(long id, long productId) throws MyEntityNotFoundException {

        Package pck = entityManager.find(Package.class, id);
        Product product = entityManager.find(Product.class, productId);

        if (pck == null){
            throw new MyEntityNotFoundException("Package with id: " + id + " not found");
        }
        if (product == null){
            throw new MyEntityNotFoundException("Product with id: " + productId + " not found");
        }

        entityManager.lock(pck, LockModeType.OPTIMISTIC);
        pck.setProduct(product);

    }
}
