package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class SensorBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long id) {
        return entityManager.find(Sensor.class, id) != null;
    }

    public Sensor find(long id) {
        return entityManager.find(Sensor.class, id);
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

    public void update(long id, String nome, long package_id) throws MyEntityNotFoundException {
        Sensor sensor = entityManager.find(Sensor.class, id);
        Package p = entityManager.find(Package.class, package_id);

        if (sensor == null)
            throw new MyEntityNotFoundException("Sensor with id: " + id + " not found");

        if (p == null)
            throw new MyEntityNotFoundException("Package with id: " + package_id + " not found");

        sensor.setName(nome);
        sensor.set_package(p);
        entityManager.merge(sensor);

    }

    public boolean delete(long id) throws MyEntityNotFoundException {

        Sensor sensor = entityManager.find(Sensor.class, id);
        if (sensor == null)
            throw new MyEntityNotFoundException("Sensor with id: " + id + " not found");

        entityManager.lock(sensor, LockModeType.OPTIMISTIC);
        entityManager.remove(sensor);
        return true;
    }

    public void addReading(long sensor_id, long reading_id) throws MyEntityNotFoundException {
        Sensor sensor = entityManager.find(Sensor.class, sensor_id);
        SensorReading sensorReading = entityManager.find(SensorReading.class, reading_id);

        if(sensor == null)
            throw new MyEntityNotFoundException("Sensor with id: " + sensor_id + " not found");
        if(sensorReading == null)
            throw new MyEntityNotFoundException("Sensor Reading with id: " + reading_id + " not found");

        //entityManager.lock(_package, LockModeType.OPTIMISTIC);
        entityManager.lock(sensor, LockModeType.OPTIMISTIC);

        sensor.addReading(sensorReading);
        entityManager.merge(sensor);
    }

    public void removeReading(long sensor_id, long reading_id) throws MyEntityNotFoundException {
        Sensor sensor = entityManager.find(Sensor.class, sensor_id);
        SensorReading sensorReading = entityManager.find(SensorReading.class, reading_id);

        if(sensor == null)
            throw new MyEntityNotFoundException("Sensor with id: " + sensor_id + " not found");
        if(sensorReading == null)
            throw new MyEntityNotFoundException("Sensor Reading with id: " + reading_id + " not found");

        //entityManager.lock(_package, LockModeType.OPTIMISTIC);
        entityManager.lock(sensor, LockModeType.OPTIMISTIC);

        sensor.removeReading(sensorReading);
        entityManager.merge(sensor);
    }

}
