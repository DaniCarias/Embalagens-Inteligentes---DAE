package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.hibernate.jpa.internal.HintsCollector;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.comparators.SensorReadingComparator;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.utils.QualityConstraintsVerifier;

import java.util.LinkedList;
import java.util.List;

@Stateless
public class SensorReadingBean {

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private EmailBean emailBean;

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
            boolean violatesConstraint = !QualityConstraintsVerifier.verifyConstraintListOnReading(
                    s.getControlledConstraints(), sensorReading);

            sensorReading.setViolatesQualityConstraint(violatesConstraint);

            // send notification email
            if(violatesConstraint) {
                /*
                // get package
                Hibernate.initialize(s.getPackage());
                Package p = s.getPackage();
                // get order
                Hibernate.initialize(s.getPackage().getOrder());
                Order o = s.getPackage().getOrder();
                // get customer
                Hibernate.initialize(s.getPackage().getOrder().getEndConsumer());
                EndConsumer e = s.getPackage().getOrder().getEndConsumer();

                // get product
                Hibernate.initialize(p.getProduct());
                Product product = p.getProduct();

                // TODO: end consumer has no email address. how to notify?
                emailBean.send(e.getAddress(), "Order no. " + o.getId() + " in risk", "Product \"" +
                        product.getName() + "\" just experienced a dangerous situation (" + s.getName() + " reading value).");
                */
            }

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

    public List<SensorReading> getViolatingSensorReadingsForProduct(long product_id) throws MyEntityNotFoundException {
        // TODO: probably this is not how it should be done. should be done with queries
        // get product by id
        Product product = entityManager.find(Product.class, product_id);
        if(product == null)
            throw new MyEntityNotFoundException("Product does not exist!");

        List<SensorReading> readings = new LinkedList<>();

        // initialize constraints list
        Hibernate.initialize(product.getQualityConstraints());

        // iterate product constraints
        for(QualityConstraint c : product.getQualityConstraints()) {

            Hibernate.initialize(c.getSensor());
            Hibernate.initialize(c.getSensor().getReadings());

            // iterating readings og the sensor monitoring the constraint
            for(SensorReading r : c.getSensor().getReadings()) {
                // add violating constraints
                if(r.doesViolateQualityConstraint())
                    readings.add(r);
            }
        }

        return readings;
    }

    public List<SensorReading> getViolatingSensorReadingsForPackage(long package_id) {
        // TODO: probably this is not how it should be done. should be done with queries

        // get package by id
        Package _package = entityManager.find(Package.class, package_id);
        Hibernate.initialize(_package.getProduct());

        List<SensorReading> readings = new LinkedList<>();

        // initialize constraints list
        Hibernate.initialize(_package.getProduct().getQualityConstraints());

        // iterate product constraints
        for(QualityConstraint c : _package.getProduct().getQualityConstraints()) {

            Hibernate.initialize(c.getSensor());
            Hibernate.initialize(c.getSensor().getReadings());

            // iterating readings og the sensor monitoring the constraint
            for(SensorReading r : c.getSensor().getReadings()) {
                // add violating constraints
                if(r.doesViolateQualityConstraint())
                    readings.add(r);
            }
        }

        return readings;
    }

    public void addSensor(long sensorReading_id, long sensor_id) throws MyEntityExistsException {
        SensorReading sensorReading = entityManager.find(SensorReading.class, sensorReading_id);
        Sensor sensor = entityManager.find(Sensor.class, sensor_id);

        if (sensorReading == null)
            throw new MyEntityExistsException("Sensor Reading with id: " + sensorReading_id + " not found");
        if (sensor == null)
            throw new MyEntityExistsException("Sensor with id: " + sensor_id + " not found");

        entityManager.lock(sensorReading, LockModeType.OPTIMISTIC);

        sensorReading.setSensor(sensor);
        entityManager.merge(sensorReading);
    }

    public void removeSensor(long sensorReading_id, long sensor_id) throws MyEntityExistsException{
        SensorReading sensorReading = entityManager.find(SensorReading.class, sensorReading_id);
        Sensor sensor = entityManager.find(Sensor.class, sensor_id);

        if (sensorReading == null)
            throw new MyEntityExistsException("Sensor Reading with id: " + sensorReading_id + " not found");
        if (sensor == null)
            throw new MyEntityExistsException("Sensor with id: " + sensor_id + " not found");

        entityManager.lock(sensorReading, LockModeType.OPTIMISTIC);

        sensorReading.setSensor(null);
        entityManager.merge(sensorReading);
    }


}
