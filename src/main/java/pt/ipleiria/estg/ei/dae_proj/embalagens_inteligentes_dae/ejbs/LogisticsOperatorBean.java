package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.EndConsumer;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.LogisticsOperator;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Hasher;

import java.util.List;

@Stateless
public class LogisticsOperatorBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;

    public boolean exists(String username) {
        return entityManager.find(LogisticsOperator.class, username) != null;
    }

    public LogisticsOperator find(String username) {
        return entityManager.find(LogisticsOperator.class, username);
    }
    public LogisticsOperator create(String username, String password, String name, String address, int phoneNumber) throws MyEntityExistsException {
        if (exists(username)) {
            throw new MyEntityExistsException("Logistics Operator with username: " + username + " already exists");
        }
        var logisticsOperator = new LogisticsOperator(username, name, hasher.hash(password), address, phoneNumber);
        entityManager.persist(logisticsOperator);
        return logisticsOperator;
    }

    public List<LogisticsOperator> getAll() {
        return entityManager.createNamedQuery("getAllLogisticsOperator", LogisticsOperator.class).getResultList();
    }

    public LogisticsOperator getLogistOperator(String username) throws MyEntityNotFoundException {
        LogisticsOperator logisticsOperator = entityManager.find(LogisticsOperator.class, username);
        if (logisticsOperator == null)
            throw new MyEntityNotFoundException("Logistcs Operator with id: " + username + " not found");
        return logisticsOperator;
    }

    public void update(String username, String password, String name, String address, int phoneNumber) {
        LogisticsOperator logisticsOperator = entityManager.find(LogisticsOperator.class, username);
        if (logisticsOperator != null) {
            logisticsOperator.setPassword(password);
            logisticsOperator.setName(name);
            logisticsOperator.setAddress(address);
            logisticsOperator.setPhoneNumber(phoneNumber);
            entityManager.merge(logisticsOperator);
        }
    }

    public boolean delete(String username) throws MyEntityNotFoundException {

        LogisticsOperator logisticsOperator = entityManager.find(LogisticsOperator.class, username);
        if (logisticsOperator == null)
            throw new MyEntityNotFoundException("logistics Operator with id: " + username + " not found");

        entityManager.lock(logisticsOperator, LockModeType.OPTIMISTIC);
        entityManager.remove(logisticsOperator);

        return true;
    }

}
