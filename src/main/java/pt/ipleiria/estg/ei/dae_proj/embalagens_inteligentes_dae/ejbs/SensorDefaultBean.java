package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Sensor;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.SensorsDefault;

import java.util.List;

@Stateless
public class SensorDefaultBean {

    @PersistenceContext
    private EntityManager entityManager;

    public List<SensorsDefault> getAllSensorsDefault() {
        return entityManager.createNamedQuery("getAllSensorsDefault", SensorsDefault.class).getResultList();
    }

    public SensorsDefault createNewSensorDefault(String name) {
        SensorsDefault sensorsDefault = new SensorsDefault(name);
        entityManager.persist(sensorsDefault);
        return sensorsDefault;
    }

}
