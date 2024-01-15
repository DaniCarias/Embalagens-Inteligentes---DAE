package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.LogisticsOperator;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.List;

public class LogisticsOperatorBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(String username) {
        return entityManager.find(LogisticsOperator.class, username) != null;
    }

    public LogisticsOperator create(String username, String password, String name, String address, int phoneNumber) throws MyEntityExistsException {
        if (exists(username)) {
            throw new MyEntityExistsException("Logistics Operator with username: " + username + " already exists");
        }
        var logisticsOperator = new LogisticsOperator(username, password, name, address, phoneNumber);
        entityManager.persist(logisticsOperator);
        return logisticsOperator;
    }

    //COMO É QUE SE FAZ A QUERY DO "getAllLogisticOperator" na Entity LogisticsOperator ??????????? se o Logistic Operator é guardado na tabela User? -> Fazer sql query com WHERE role = "LogisticOperator" ???????
    public List<LogisticsOperator> getAll() {
        //return entityManager.createNamedQuery("getAllLogisticsoperator", LogisticsOperator.class).getResultList();
        return null;
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

    public void delete(String username) throws MyEntityNotFoundException {

        LogisticsOperator logisticsOperator = entityManager.find(LogisticsOperator.class, username);
        if (logisticsOperator == null)
            throw new MyEntityNotFoundException("logistics Operator with id: " + username + " not found");

        //entityMannager.lock(order, LockModeType.OPTIMISTIC); ???????????
        entityManager.remove(logisticsOperator);
    }

}
