package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    private ProductBean productBean;
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() {
        try{

            productBean.create("nome do produto1", "descricao do produto1", null);
            productBean.create("nome do produto2", "descricao do produto2", null);
            productBean.create("nome do produto3", "descricao do produto3", null);





        }catch(Exception exception){
            logger.severe(exception.getMessage());
        }
    }








}