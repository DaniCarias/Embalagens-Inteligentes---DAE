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
    @EJB
    private SensorDefaultBean sensorDefaultBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        try{

        //Sensors Default
            SensorsDefault sensorDefault1 = sensorDefaultBean.createNewSensorDefault("Temperatura");
            SensorsDefault sensorDefault2 = sensorDefaultBean.createNewSensorDefault("Humidade");
            SensorsDefault sensorDefault3 = sensorDefaultBean.createNewSensorDefault("Luminosidade");
            SensorsDefault sensorDefault4 = sensorDefaultBean.createNewSensorDefault("Pressão");
            SensorsDefault sensorDefault5 = sensorDefaultBean.createNewSensorDefault("Peso");




        //Product Manufactor
            ProductManufacturer productManufacturer1 = productManufacturerBean.create("danicarias", "pass123", "Daniel Carias", "urbanização", 961234567);
            ProductManufacturer productManufacturer2 = productManufacturerBean.create("pedro_dev", "pass123", "Daniel Carias", "urbanização", 961234567);
            ProductManufacturer productManufacturer3 = productManufacturerBean.create("manufacturer", "pass123", "Test Manufacturer", "acolá", 961234567);

        //Product
            Product prod1 = productBean.create("nome do produto 1", "descricao do produto 1", productManufacturer1);
            Product prod2 = productBean.create("nome do produto 2", "descricao do produto 2", productManufacturer1);
            Product prod3 = productBean.create("nome do produto 3", "descricao do produto 3", productManufacturer2);
            Product prod4 = productBean.create("nome do produto 4", "descricao do produto 4", productManufacturer1);
            Product prod5 = productBean.create("nome do produto 5", "descricao do produto 5", productManufacturer2);

        //Package
            Package package1 = packageBean.create(Package.PackageType.PRIMARIA, Date.from(java.time.Instant.now()), "Madeira");
            Package package2 = packageBean.create(Package.PackageType.PRIMARIA, Date.from(java.time.Instant.now()), "Ferro");
            Package package3 = packageBean.create(Package.PackageType.PRIMARIA, Date.from(java.time.Instant.now()), "Plastico");
            Package package4 = packageBean.create(Package.PackageType.PRIMARIA, Date.from(java.time.Instant.now()), "Madeira");

            package1.setProduct(prod1);
            package2.setProduct(prod2);
            package3.setProduct(prod3);
            package4.setProduct(prod4);

            prod1.setPackage(package1);
            prod2.setPackage(package2);
            prod3.setPackage(package3);
            prod4.setPackage(package4);

        //End Consumer
            EndConsumer endConsumer1 = endConsumerBean.create("danicarias_outro", "pass123", "Daniel Carias", "urbanização", 961234567);
            EndConsumer endConsumer2 = endConsumerBean.create("danicarias_outro2", "teste2", "Daniel teste2", "test2", 919123111);
            EndConsumer endConsumer3 = endConsumerBean.create("end_consumer", "pass123", "Test End Consumer", "acolá", 919123111);

        //Order
            Order order1 = orderBean.create(endConsumer1);
            Order order2 = orderBean.create(endConsumer1);
            Order order3 = orderBean.create(endConsumer2);

            packageBean.addOrder(package1.getId(), order1.getId());
            packageBean.addOrder(package2.getId(), order2.getId());
            packageBean.addOrder(package3.getId(), order2.getId());

        //Logistic Operator
            LogisticsOperator logisticsOperator1 = logisticsOperatorBean.create("danicarias_teste", "pass123", "Daniel Carias", "urbanização", 961234567);
            LogisticsOperator logisticsOperator2 = logisticsOperatorBean.create("logistic_operator", "pass123", "Test Logistic Operator", "acolá", 961234567);

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