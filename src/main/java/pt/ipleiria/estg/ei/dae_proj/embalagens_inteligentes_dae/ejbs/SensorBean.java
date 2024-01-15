package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.EndConsumer;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Sensor;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.List;

public class SensorBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long id) {
        return entityManager.find(Sensor.class, id) != null;
    }

    public Sensor create(String name, long package_id) throws MyEntityNotFoundException {
        // find package by id
        Package p = entityManager.find(Package.class, package_id);
        if(p == null)
            throw new MyEntityNotFoundException("The specified package does not exist!");

        var sensor = new Sensor(name, p);
        entityManager.persist(sensor);
        return sensor;
    }

    public List<Sensor> getAllSensor() {
        return entityManager.createNamedQuery("getAllSensors", Sensor.class).getResultList();
    }

    public Sensor getSensor(String name) throws MyEntityNotFoundException {
        Sensor sensor = entityManager.find(Sensor.class, name);
        if (sensor == null)
            throw new MyEntityNotFoundException("Sensor with name: " + name + " not found");
        return sensor;
    }

    public void update(String name) {
        Sensor sensor = entityManager.find(Sensor.class, name);
        if (sensor != null) {
            sensor.setName(name);
            entityManager.merge(sensor);
        }
    }

    public void delete(String name) throws MyEntityNotFoundException {

        Sensor sensor = entityManager.find(Sensor.class, name);
        if (sensor == null)
            throw new MyEntityNotFoundException("Sensor with name: " + name + " not found");

        //entityMannager.lock(order, LockModeType.OPTIMISTIC); ???????????
        entityManager.remove(sensor);
    }

}
