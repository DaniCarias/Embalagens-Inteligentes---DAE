package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;

import java.util.Date;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    private ProductBean productBean;
    @EJB
    private OrderBean orderBean;
    @EJB
    private PackageBean packageBean;
    @EJB
    private EndConsumerBean endConsumerBean;
    @EJB
    private ProductManufacturerBean productManufacturerBean;
    @EJB
    private LogisticsOperatorBean logisticsOperatorBean;
    @EJB
    private SensorBean sensorBean;
    @EJB
    private QualityConstraintBean qualityConstraintBean;
    @EJB
    private SensorReadingBean sensorReadingBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        try{

        //Product Manufactor
            ProductManufacturer productManufacturer1 = productManufacturerBean.create("danicarias", "pass123", "Daniel Carias", "urbanização", 961234567);
            ProductManufacturer productManufacturer2 = productManufacturerBean.create("pedro_dev", "pass123", "Daniel Carias", "urbanização", 961234567);

        //Product
            Product prod1 = productBean.create("nome do produto1", "descricao do produto1", productManufacturer1);
            Product prod2 = productBean.create("nome do produto2", "descricao do produto2", productManufacturer1);
            Product prod3 = productBean.create("nome do produto123123123", "descricao do produto3", productManufacturer2);
            Product prod4 = productBean.create("produto diferente", "descricao do produto4", productManufacturer1);

        //Package
            Package package1 = packageBean.create(Package.PackageType.PRIMARIA, Date.from(java.time.Instant.now()), "Madeira");
            Package package2 = packageBean.create(Package.PackageType.PRIMARIA, Date.from(java.time.Instant.now()), "Ferro");
            package1.setProduct(prod1);
            package2.setProduct(prod2);
            prod1.setPackage(package1);
            prod2.setPackage(package2);

        //End Consumer
            EndConsumer endConsumer1 = endConsumerBean.create("danicarias_outro", "pass123", "Daniel Carias", "urbanização", 961234567);
            EndConsumer endConsumer2 = endConsumerBean.create("danicarias_outro2", "teste2", "Daniel teste2", "test2", 919123111);

        //Order
            Order order1 = orderBean.create(endConsumer1);
            Order order2 = orderBean.create(endConsumer2);

            packageBean.addOrder(package1.getId(), order1.getId());

        //Logistic Operator
            LogisticsOperator logisticsOperator1 = logisticsOperatorBean.create("danicarias_teste", "pass123", "Daniel Carias", "urbanização", 961234567);

        //Sensor
            // belonging to package1
            Sensor sensor1 = sensorBean.create("sensor_teste", package1.getId());
            Sensor sensor2 = sensorBean.create("sensor_teste1", package1.getId());

            // belonging to package2
            Sensor sensor3 = sensorBean.create("sensor_teste2", package2.getId());

        //Quality Constraint
            QualityConstraint constraint1 = qualityConstraintBean.create(20.0f,
                    QualityConstraint.ConstraintType.LOWER, prod1.getId());
            QualityConstraint constraint2 = qualityConstraintBean.create(10.0f,
                    QualityConstraint.ConstraintType.UPPER, prod1.getId());

            // associate sensors
            qualityConstraintBean.setSensor(constraint1.getId(), sensor1.getId());
            qualityConstraintBean.setSensor(constraint2.getId(), sensor1.getId());

        //Sensor Reading
            SensorReading reading1 = sensorReadingBean.create(15.0f, sensor1.getId());
            SensorReading reading2 = sensorReadingBean.create(25.0f, sensor1.getId());

            SensorReading reading3 = sensorReadingBean.create(700.0f, sensor2.getId());
            SensorReading reading4 = sensorReadingBean.create(682.3f, sensor2.getId());

            SensorReading reading5 = sensorReadingBean.create(34.65f, sensor3.getId());

        }catch(Exception exception){
            logger.severe(exception.getMessage());
        }
    }








}