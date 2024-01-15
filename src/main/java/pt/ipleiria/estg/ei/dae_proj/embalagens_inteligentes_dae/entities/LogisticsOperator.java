package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("LogisticsOperator")
public class LogisticsOperator extends User {

    public LogisticsOperator(String username, String name, String password, String address, int phoneNumber) {
        super(username, name, password, address, phoneNumber);
    }

    public LogisticsOperator() {

    }

    /*
    - Localização e rastreamento: dados relativos à localização em tempo real e rastreamento
    de determinada encomenda para, por exemplo, otimizar a rota e garantir horários de entregas;
    - Condições Ambientais: dados sobre temperatura, humidade, acelerações e outros fatores
    ambientais durante o transporte garantem que os produtos são manuseados de acordo com as
    especificações;
    - Alertas de segurança: dados de segurança, como deteção de abertura e alertas de acesso
    não autorizado, para ajudar os operadores de logística a garantirem a segurança e a integridade
    das embalagens durante o transporte;
     */

}
