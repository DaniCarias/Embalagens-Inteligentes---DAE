package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    public QualityConstraint create(float value, QualityConstraint.ConstraintType type, long sensor_id) throws MyEntityNotFoundException {

        // find the sensor controlling this constraint
        Sensor s = entityManager.find(Sensor.class, sensor_id);
        if(s == null)
            throw new MyEntityNotFoundException("Specified sensor does not exist!");

        QualityConstraint constraint = new QualityConstraint(value, type, s);

        entityManager.persist(constraint);
        return constraint;
    }

    public List<QualityConstraint> getAllConstraints() {
        return entityManager.createNamedQuery("getAllQualityConstraints", QualityConstraint.class).getResultList();
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
