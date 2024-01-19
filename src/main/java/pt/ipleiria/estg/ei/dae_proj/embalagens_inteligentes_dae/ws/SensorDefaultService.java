package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.SensorDefaultDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.SensorReadingDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.SensorDefaultBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.SensorReadingBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.SensorReading;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.SensorsDefault;

import java.util.List;
import java.util.stream.Collectors;

@Path("sensordefaults")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
//@Authenticated
public class SensorDefaultService {

    @EJB
    private SensorDefaultBean sensorDefaultBean;

    private SensorDefaultDTO toDTO(SensorsDefault sensorsDefault) {
        return new SensorDefaultDTO(
                sensorsDefault.getId(),
                sensorsDefault.getName()
        );
    }

    private List<SensorDefaultDTO> toDTOs(List<SensorsDefault> sensorsDefaults) {
        return sensorsDefaults.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public Response getAllSensorDefault() {
        return Response.status(Response.Status.OK).entity(toDTOs(sensorDefaultBean.getAllSensorsDefault())).build();
    }


}
