package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Sensor;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.SensorReading;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class SensorReadingBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long id) {
        return entityManager.find(SensorReading.class, id) != null;
    }

    public SensorReading create(float value) throws MyEntityExistsException {
        var sensorReading = new SensorReading(value);
        entityManager.persist(sensorReading);
        return sensorReading;
    }

    //Verificar onde é q se vai buscar o getAllSensoresReading
    public List<Sensor> getAllSensor() {
        //return entityManager.createNamedQuery("getAllSensoresReading", SensorReading.class).getResultList();
        return null;
    }

    public SensorReading getSensorReading(long id) throws MyEntityNotFoundException {
        SensorReading sensorReading = entityManager.find(SensorReading.class, id);
        if (sensorReading == null)
            throw new MyEntityNotFoundException("Sensor Reading with id: " + id + " not found");
        return sensorReading;
    }

    public void update(float value, long id) {
        SensorReading sensorReading = entityManager.find(SensorReading.class, id);
        if (sensorReading != null) {
            sensorReading.setValue(value);
            entityManager.merge(sensorReading);
        }
    }

    public void delete(long id) throws MyEntityNotFoundException {

        SensorReading sensorReading = entityManager.find(SensorReading.class, id);
        if (sensorReading == null)
            throw new MyEntityNotFoundException("Sensor with id: " + id + " not found");

        //entityMannager.lock(order, LockModeType.OPTIMISTIC); ???????????
        entityManager.remove(sensorReading);
    }

}
