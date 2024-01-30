package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Lock;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Product;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.QualityConstraint;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Sensor;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class QualityConstraintBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long id) {
        return entityManager.find(QualityConstraint.class, id) != null;
    }

    public QualityConstraint find(long id) {
        return entityManager.find(QualityConstraint.class, id);
    }

    public QualityConstraint create(float value, QualityConstraint.ConstraintType type, long product_id) throws MyEntityNotFoundException {

        Product p = entityManager.find(Product.class, product_id);
        if(p == null)
            throw new MyEntityNotFoundException("PRODUCT_NOT_FOUND");

        QualityConstraint constraint = new QualityConstraint(value, type, p);

        entityManager.persist(constraint);
        return constraint;
    }

    public List<QualityConstraint> getAllConstraints() {
        return entityManager.createNamedQuery("getAllQualityConstraints", QualityConstraint.class).getResultList();
    }

    public List<QualityConstraint> getAllConstraintsForProduct(long product_id) {
        return entityManager.createNamedQuery("getAllQualityConstraintsForProduct", QualityConstraint.class)
                .setParameter("productId", product_id)
                .getResultList();
    }

    public List<QualityConstraint> getAllConstraintsForSensor(long sensor_id) {
        return entityManager.createNamedQuery("getAllQualityConstraintsForSensor", QualityConstraint.class)
                .setParameter("sensorId", sensor_id)
                .getResultList();
    }

    public List<QualityConstraint> getAllConstraintsForOrder(long order_id) {
        return entityManager.createNamedQuery("getAllQualityConstraintsForOrder", QualityConstraint.class)
                .setParameter("orderId", order_id)
                .getResultList();
    }

    public List<QualityConstraint> getAllConstraintsForPackage(long package_id) {
        return entityManager.createNamedQuery("getAllQualityConstraintsForPackage", QualityConstraint.class)
                .setParameter("packageId", package_id)
                .getResultList();
    }

    public QualityConstraint getConstraint(long id) throws MyEntityNotFoundException {
        QualityConstraint constraint = entityManager.find(QualityConstraint.class, id);
        if(constraint == null)
            throw new MyEntityNotFoundException("Quality constraint does not exist!");
        return constraint;
    }

    public void update(long id, float value, QualityConstraint.ConstraintType type, long sensor_id) throws MyEntityNotFoundException {

        // find sensor
        Sensor s = entityManager.find(Sensor.class, sensor_id);
        if(s == null)
            throw new MyEntityNotFoundException("SENSOR_NOT_FOUND");

        QualityConstraint c = getConstraint(id);
        if(c != null) {
            c.setValue(value);
            c.setType(type);
            c.setSensor(s);

            entityManager.lock(c, LockModeType.OPTIMISTIC);
            entityManager.merge(c);
        }
    }

    // associate a sensor to this constraint
    public void setSensor(long constraint_id, long sensor_id) throws MyEntityNotFoundException {

        // get constraint by ID
        QualityConstraint c = entityManager.find(QualityConstraint.class, constraint_id);
        if(c == null)
            throw new MyEntityNotFoundException("CONSTRAINT_NOT_FOUND");

        // get sensor by id
        Sensor s = entityManager.find(Sensor.class, sensor_id);
        if(s == null)
            throw new MyEntityNotFoundException("SENSOR_NOT_FOUND");

        // update the sensor and persist
        entityManager.lock(c, LockModeType.OPTIMISTIC);
        c.setSensor(s);
        entityManager.merge(c);
    }

    public void removeSensor(long constraint_id) throws MyEntityNotFoundException {

        // get constraint by ID
        QualityConstraint c = entityManager.find(QualityConstraint.class, constraint_id);
        if(c == null)
            throw new MyEntityNotFoundException("CONSTRAINT_NOT_FOUND");

        // remove the sensor
        c.setSensor(null);

        // persist the change
        entityManager.lock(c, LockModeType.OPTIMISTIC);
        entityManager.merge(c);
    }

    public void delete(long id) throws MyEntityNotFoundException {

        QualityConstraint c = entityManager.find(QualityConstraint.class, id);
        if(c == null)
            throw new MyEntityNotFoundException("Quality constraint does not exist!");

        entityManager.lock(c, LockModeType.OPTIMISTIC);
        entityManager.remove(c);
    }
}
