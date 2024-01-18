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
            ProductManufacturer productManufacturer1 = productManufacturerBean.create("danicarias",
                    "pass123", "Daniel Carias", "urbanização", 961234567);

        //Product
            Product prod1 = productBean.create("nome do produto1", "descricao do produto1",
                    productManufacturer1);
            Product prod2 = productBean.create("nome do produto2", "descricao do produto2",
                    productManufacturer1);
            Product prod3 = productBean.create("nome do produto123123123", "descricao do produto3",
                    productManufacturer1);

        //Package
            Package package1 = packageBean.create(Package.PackageType.PRIMARIA, Date.from(java.time.Instant.now()),
                    "Madeira", prod1);
            Package package2 = packageBean.create(Package.PackageType.PRIMARIA, Date.from(java.time.Instant.now()),
                    "Ferro", prod2);
            prod1.setPackage(package1);
            prod2.setPackage(package2);

        //End Consumer
            EndConsumer endConsumer1 = endConsumerBean.create("danicarias_outro", "pass123",
                    "Daniel Carias", "urbanização", 961234567, "2211086@my.ipleiria.pt");
            EndConsumer endConsumer2 = endConsumerBean.create("danicarias_outro2", "teste2",
                    "Daniel teste2", "test2", 919123111, "2211086@my.ipleiria.pt");

        //Order
            Order order1 = orderBean.create(endConsumer1);
            Order order2 = orderBean.create(endConsumer2);

            packageBean.addOrder(package1.getId(), order1.getId());

        //Logistic Operator
            LogisticsOperator logisticsOperator1 = logisticsOperatorBean.create("danicarias_teste",
                    "pass123", "Daniel Carias", "urbanização", 961234567);

        //Sensor
            Sensor sensor1 = sensorBean.create("sensor_teste", package1.getId());

        //Quality Constraint
            QualityConstraint constraint1 = qualityConstraintBean.create(20.0f,
                    QualityConstraint.ConstraintType.LOWER, sensor1.getId(), prod1.getId());
            QualityConstraint constraint2 = qualityConstraintBean.create(10.0f,
                    QualityConstraint.ConstraintType.UPPER, sensor1.getId(), prod1.getId());

        //Sensor Reading
            SensorReading reading1 = sensorReadingBean.create(15.0f, sensor1.getId());
            SensorReading reading2 = sensorReadingBean.create(25.0f, sensor1.getId());

        }catch(Exception exception){
            logger.severe(exception.getMessage());
        }
    }








}