package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.EndConsumer;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Order;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.Date;
import java.util.List;

@Stateless
public class OrderBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long id) {
        Query query = entityManager.createQuery("SELECT COUNT(o.id) FROM Order o WHERE o.id = :id", Long.class);
        query.setParameter("id", id);
        return (Long)query.getSingleResult() > 0L;
    }

    public Order find(long id) {
        return entityManager.find(Order.class, id);
    }

    public boolean endConsumer_verify(EndConsumer endConsumer) {
        EndConsumer end_consumer = entityManager.find(EndConsumer.class, endConsumer.getUsername());
        return end_consumer != null ? true : false;
    }

    public Order create(EndConsumer endConsumer) throws MyEntityNotFoundException {

        if (!endConsumer_verify(endConsumer))
            throw new MyEntityNotFoundException("End Consumer with username: " + endConsumer.getUsername() + " not found");

        Order order = new Order(endConsumer);
        entityManager.persist(order);
        return order;
    }

    public List<Order> getAll() {
        return entityManager.createNamedQuery("getAllOrders", Order.class).getResultList();
    }

    public List<Order> getOrderByEndConsumer(String username) {
        return entityManager.createNamedQuery("getOrdersByEndConsumer", Order.class).setParameter("username", username).getResultList();
    }

    public List<Order> getOrderByIdByEndConsumer(String username, long id) {
        return entityManager.createNamedQuery("getOrdersByIdByEndConsumer", Order.class).setParameter("username", username).setParameter("id", id).getResultList();
    }

    public void update(long id, EndConsumer endConsumer) throws MyEntityNotFoundException {

        if (!exists(id))
            throw new MyEntityNotFoundException("Order with id: " + id + " not found");

        if (!endConsumer_verify(endConsumer))
            throw new MyEntityNotFoundException("End Consumer with username: " + endConsumer.getUsername() + " not found");

        Order order = find(id);

        entityManager.lock(order, LockModeType.OPTIMISTIC);
        order.setEndConsumer(endConsumer);
    }

    public boolean delete(long id) throws MyEntityNotFoundException {

        Order order = entityManager.find(Order.class, id);
        if (order == null)
            throw new MyEntityNotFoundException("Order with id: " + id + " not found");

        order.setDeleted_at(new Date());
        entityManager.persist(order);
        entityManager.flush();

        entityManager.lock(order, LockModeType.OPTIMISTIC);
        entityManager.remove(order);
        return true;
    }

    public boolean addPackage(long order_id, long package_id) throws MyEntityNotFoundException {
        Package _package = entityManager.find(Package.class, package_id);
        Order order = entityManager.find(Order.class, order_id);

        if(_package == null)
            throw new MyEntityNotFoundException("Package with id: " + package_id + " not found");
        if(order == null)
            throw new MyEntityNotFoundException("Order with id: " + order_id + " not found");

        entityManager.lock(order, LockModeType.OPTIMISTIC);
        order.addPackage(_package);

        return order.getPackages().contains(_package);
    }

    public void removePackage(long order_id) throws MyEntityNotFoundException {
        Order order = entityManager.find(Order.class, order_id);

        if (order == null)
            throw new MyEntityNotFoundException("Order with id: " + order_id + " not found");

        entityManager.lock(order, LockModeType.OPTIMISTIC);
        order.setPackage(null);
    }

    public Order getOrderPackages(long id) throws MyEntityNotFoundException{
        Order order = entityManager.find(Order.class, id);
        if(!exists(order.getId())){
            throw new MyEntityNotFoundException("Order with id: " + id + " not found");
        }
        Hibernate.initialize(order.getPackage());
        return order;
    }



}
