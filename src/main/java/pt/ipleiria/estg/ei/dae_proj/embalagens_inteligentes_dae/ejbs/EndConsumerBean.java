package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.EndConsumer;
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

    //COMO É QUE SE FAZ A QUERY DO "getAllEndConsumers" na Entity EndConsumer ??????????? se o EndConsumer é guardado na tabela User? -> Fazer sql query com WHERE role = "EndConsumer" ???????
    public List<EndConsumer> getAll() {
        //return entityManager.createNamedQuery("getAllEndConsumers", EndConsumer.class).getResultList();
        return null;
    }

    public EndConsumer getEndConsumer(String username) throws MyEntityNotFoundException {
        EndConsumer endConsumer = entityManager.find(EndConsumer.class, username);
        if (endConsumer == null)
            throw new MyEntityNotFoundException("EndConsumer with id: " + username + " not found");
        return endConsumer;
    }

    public void update(String username, String password, String name, String address, int phoneNumber) {
        EndConsumer endConsumer = entityManager.find(EndConsumer.class, username);
        if (endConsumer != null) {
            endConsumer.setPassword(password);
            endConsumer.setName(name);
            endConsumer.setAddress(address);
            endConsumer.setPhoneNumber(phoneNumber);
            entityManager.merge(endConsumer);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {

        EndConsumer endConsumer = entityManager.find(EndConsumer.class, username);
        if (endConsumer == null)
            throw new MyEntityNotFoundException("EndConsumer with id: " + username + " not found");

        //entityMannager.lock(order, LockModeType.OPTIMISTIC); ???????????
        entityManager.remove(endConsumer);
    }


}
