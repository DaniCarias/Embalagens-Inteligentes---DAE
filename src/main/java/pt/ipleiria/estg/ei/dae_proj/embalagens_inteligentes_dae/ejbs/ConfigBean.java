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

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        try{

        //Product Manufactor
            ProductManufacturer productManufacturer1 = productManufacturerBean.create("danicarias", "pass123", "Daniel Carias", "urbanização", 961234567);

        //Product
            Product prod1 = productBean.create("nome do produto1", "descricao do produto1", productManufacturer1);
            Product prod2 = productBean.create("nome do produto2", "descricao do produto2", productManufacturer1);
            Product prod3 = productBean.create("nome do produto123123123", "descricao do produto3", productManufacturer1);

        //End Consumer
            EndConsumer endConsumer1 = endConsumerBean.create("danicarias_outro", "pass123", "Daniel Carias", "urbanização", 961234567);

        //Order
            orderBean.create(endConsumer1);

        //Package
            Package package1 = packageBean.create(Package.PackageType.PRIMARIA, Date.from(java.time.Instant.now()), "Madeira", prod1);

        //Logistic Operator
            LogisticsOperator logisticsOperator1 = logisticsOperatorBean.create("danicarias_teste", "pass123", "Daniel Carias", "urbanização", 961234567);

        //Sensor
            Sensor sensor1 = sensorBean.create("sensor_teste");

        }catch(Exception exception){
            logger.severe(exception.getMessage());
        }
    }








}