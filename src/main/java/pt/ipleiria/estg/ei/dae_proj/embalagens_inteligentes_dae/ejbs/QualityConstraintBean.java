package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Product;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.QualityConstraint;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Sensor;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

import java.util.List;

public class QualityConstraintBean {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean exists(long id) {
        return entityManager.find(QualityConstraint.class, id) != null;
    }

    public QualityConstraint find(long id) {
        return entityManager.find(QualityConstraint.class, id);
    }

    public QualityConstraint create(float value, QualityConstraint.ConstraintType type, long sensor_id, long product_id) throws MyEntityNotFoundException {

        // find the sensor controlling this constraint
        Sensor s = entityManager.find(Sensor.class, sensor_id);
        if(s == null)
            throw new MyEntityNotFoundException("Specified sensor with ID " + sensor_id + " does not exist!");

        Product p = entityManager.find(Product.class, product_id);
        if(p == null)
            throw new MyEntityNotFoundException("Specified product with ID " + product_id + " does not exist!");

        QualityConstraint constraint = new QualityConstraint(value, type, s, p);

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

    public void update(long id, float value, QualityConstraint.ConstraintType type) throws MyEntityNotFoundException {

        QualityConstraint c = getConstraint(id);
        if(c != null) {
            c.setValue(value);
            c.setType(type);
            entityManager.merge(c);
        }
    }

    public void delete(long id) throws MyEntityNotFoundException {

        QualityConstraint c = entityManager.find(QualityConstraint.class, id);
        if(c == null)
            throw new MyEntityNotFoundException("Quality constraint does not exist!");

        entityManager.remove(c);
    }
}
