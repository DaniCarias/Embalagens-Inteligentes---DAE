package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.EndConsumer;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Order;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EndConsumerBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(String username) {
        return entityManager.find(EndConsumer.class, username) != null;
    }

    public EndConsumer find(String username) {
        return entityManager.find(EndConsumer.class, username);
    }

    public EndConsumer create(String username, String password, String name, String address, int phoneNumber) throws MyEntityExistsException {
        if (exists(username)) {
            throw new MyEntityExistsException("End Consumer with username: " + username + " already exists");
        }
        var endConsumer = new EndConsumer(username, password, name, address, phoneNumber);
        entityManager.persist(endConsumer);
        return endConsumer;
    }

    public List<EndConsumer> getAll() {
        return entityManager.createNamedQuery("getAllEndConsumers", EndConsumer.class).getResultList();
    }

    public EndConsumer getEndConsumer(String username) throws MyEntityNotFoundException {
        EndConsumer endConsumer = entityManager.find(EndConsumer.class, username);
        if (endConsumer == null)
            throw new MyEntityNotFoundException("EndConsumer with id: " + username + " not found");
        return endConsumer;
    }

    public void update(String username, String name, String address, int phoneNumber) {
        EndConsumer endConsumer = entityManager.find(EndConsumer.class, username);
        if (endConsumer != null) {
            //endConsumer.setPassword(password);
            endConsumer.setName(name);
            endConsumer.setAddress(address);
            endConsumer.setPhoneNumber(phoneNumber);
            entityManager.merge(endConsumer);
        }
    }

    public boolean delete(String username) throws MyEntityNotFoundException {

        EndConsumer endConsumer = entityManager.find(EndConsumer.class, username);
        if (endConsumer == null)
            throw new MyEntityNotFoundException("EndConsumer with id: " + username + " not found");

        entityManager.lock(endConsumer, LockModeType.OPTIMISTIC);

        entityManager.remove(endConsumer);
        return true;
    }

    public void addOrder(String username, long order_id) throws MyEntityNotFoundException{
        Order order = entityManager.find(Order.class, order_id);
        EndConsumer endConsumer = entityManager.find(EndConsumer.class, username);

        if(order == null)
            throw new MyEntityNotFoundException("Order with id: " + order_id + " not found");
        if(endConsumer == null)
            throw new MyEntityNotFoundException("EndConsumer with id: " + username + " not found");

        entityManager.lock(endConsumer, LockModeType.OPTIMISTIC);
        //entityManager.lock(order, LockModeType.OPTIMISTIC);

        endConsumer.addOrder(order);
        entityManager.merge(endConsumer);
    }

    public void removeOrder(String username, long order_id) throws MyEntityNotFoundException{
        Order order = entityManager.find(Order.class, order_id);
        EndConsumer endConsumer = entityManager.find(EndConsumer.class, username);

        if(order == null)
            throw new MyEntityNotFoundException("Order with id: " + order_id + " not found");
        if(endConsumer == null)
            throw new MyEntityNotFoundException("EndConsumer with id: " + username + " not found");

        entityManager.lock(endConsumer, LockModeType.OPTIMISTIC);
        //entityManager.lock(order, LockModeType.OPTIMISTIC);

        endConsumer.removeOrder(order);
        entityManager.merge(endConsumer);
    }

    public EndConsumer getEndConsumerOrders(String username) throws MyEntityNotFoundException{
        EndConsumer endConsumer = entityManager.find(EndConsumer.class, username);
        if(!exists(endConsumer.getUsername())){
            throw new MyEntityNotFoundException("End Consumer with username: " + username + " not found");
        }
        Hibernate.initialize(endConsumer.getOrders());
        return endConsumer;
    }









}
