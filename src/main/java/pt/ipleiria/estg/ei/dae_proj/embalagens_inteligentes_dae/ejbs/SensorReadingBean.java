package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.comparators.SensorReadingComparator;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Sensor;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.SensorReading;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.utils.QualityConstraintsVerifier;

import java.util.List;

public class SensorReadingBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long id) {
        return entityManager.find(SensorReading.class, id) != null;
    }

    public SensorReading create(float value, long sensor_id) throws MyEntityNotFoundException {
        // find the sensor by ID
        Sensor s = entityManager.find(Sensor.class, sensor_id);
        if(s == null)
            throw new MyEntityNotFoundException("Specified tensor does not exist!");

        SensorReading sensorReading = new SensorReading(value, s);

        // check the constraints this sensor controls
        try {
            sensorReading.setViolatesQualityConstraint(!QualityConstraintsVerifier.verifyConstraintListOnReading(
                    s.getControlledConstraints(), sensorReading));
        } catch (Exception e) {
            System.err.println("Given constraint was invalid!");
        }

        entityManager.persist(sensorReading);
        return sensorReading;
    }

    //Verificar onde é q se vai buscar o getAllSensoresReading
    public List<SensorReading> getAllSensorReadings() {
        return entityManager.createNamedQuery("getAllSensorReadings", SensorReading.class).getResultList();
    }

    public SensorReading getSensorReading(long id) throws MyEntityNotFoundException {
        SensorReading sensorReading = entityManager.find(SensorReading.class, id);
        if (sensorReading == null)
            throw new MyEntityNotFoundException("Sensor Reading with id: " + id + " not found");
        return sensorReading;
    }
}
