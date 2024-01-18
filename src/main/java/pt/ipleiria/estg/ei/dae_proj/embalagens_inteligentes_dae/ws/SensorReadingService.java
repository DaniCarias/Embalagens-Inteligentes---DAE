package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.SensorReadingDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.SensorBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.SensorReadingBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.SensorReading;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("sensor_readings")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class SensorReadingService {

    @EJB
    private SensorReadingBean sensorReadingBean;

    @EJB
    private SensorBean sensorBean;

    private SensorReadingDTO toDTO(SensorReading sensorReading) {
        return new SensorReadingDTO(
                sensorReading.getId(),
                sensorReading.getDate(),
                sensorReading.getValue(),
                sensorReading.getSensor().getId()
        );
    }

    private List<SensorReadingDTO> toDTOs(List<SensorReading> sensorReadings) {
        return sensorReadings.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<SensorReadingDTO> getAllSensorReadings() {
        return toDTOs(sensorReadingBean.getAllSensorReadings());
    }

    @POST
    @Path("/")
    public Response addNewSensorReading(SensorReadingDTO sensorReadingDTO) {
        try {
            SensorReading sensorReading = sensorReadingBean.create(sensorReadingDTO.getValue(), sensorReadingDTO.getSensor_id());
            if(sensorReading == null)
                return Response.status(Response.Status.NOT_FOUND).build();

            return Response.status(Response.Status.CREATED).entity(toDTO(sensorReading)).build();
        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/sensor/{sensor_id}")
    public Response getSensorReadingsForSensor(@PathParam("sensor_id") long sensor_id) {

        List<SensorReading> sensorReadings = sensorReadingBean.getSensorReadingsForSensor(sensor_id);

        return Response.status(Response.Status.CREATED).entity(toDTOs(sensorReadings)).build();
    }

    @GET
    @Path("/product/{product_id}")
    public Response getSensorReadingsForProduct(@PathParam("product_id") long product_id) {

        List<SensorReading> sensorReadings = sensorReadingBean.getViolatingSensorReadingsForProduct(product_id);

        return Response.status(Response.Status.CREATED).entity(toDTOs(sensorReadings)).build();
    }

}
