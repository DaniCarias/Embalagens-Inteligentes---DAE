package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.OrderDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.PackageBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.SensorBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.SensorReadingBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Authenticated;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class SensorService {

    @EJB
    private SensorBean sensorBean;
    @EJB
    private PackageBean packageBean;
    @EJB
    private SensorReadingBean sensorReadingBean;

    private SensorDTO toDTO(Sensor sensor) {

        return new SensorDTO(
                sensor.getId(),
                sensor.getName(),
                sensor.getPackage().getId()
        );
    }

    private List<SensorDTO> toDTOs(List<Sensor> sensors) {
        return sensors.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @RolesAllowed({"ProductManufacturer", "LogisticsOperator"})
    @GET
    @Path("/")
    public List<SensorDTO> getAllSensors() {
        return toDTOs(sensorBean.getAllSensor());
    }


    @RolesAllowed({"ProductManufacturer", "LogisticsOperator"})
    @GET
    @Path("/manufacturer/{username}")
    public List<SensorDTO> getSensorsByManufactor(@PathParam("username") String username) {
        return toDTOs(sensorBean.getSensorsByManufaturer(username));
    }


    @RolesAllowed({"ProductManufacturer", "LogisticsOperator", "EndConsumer"})
    @GET
    @Path("/{id}")
    public Response getSensorDetails(@PathParam("id") long id) {
        Sensor sensor = sensorBean.find(id);
        if (sensor != null) {
            return Response.status(Response.Status.OK).entity(toDTO(sensor)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Sensor do not exist").build();
    }

    @RolesAllowed({"ProductManufacturer"})
    @POST
    @Path("/")
    public Response createNewSensor(SensorDTO sensorDTO) {
        Package pck = packageBean.find(sensorDTO.getPackage_id());
        if(pck == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        try {
            Sensor sensor = sensorBean.create(sensorDTO.getName(), pck.getId());

            if(sensor == null)
                return Response.status(Response.Status.BAD_REQUEST).build();
            sensor = sensorBean.find(sensor.getId());

            return Response.status(Response.Status.CREATED).entity(toDTO(sensor)).build();
        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @RolesAllowed({"ProductManufacturer"})
    @PUT
    @Path("/{id}")
    public Response editSensor(@PathParam("id") long id, SensorDTO sensorDTO) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.find(id);
        if(sensor == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor do not exist").build();

        Package pck = packageBean.find(sensorDTO.getPackage_id());
        if(pck == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        sensorBean.update(id, sensorDTO.getName(), sensorDTO.getPackage_id());

        return Response.status(Response.Status.OK).entity(toDTO(sensor)).entity("Sensor updated").build();
    }

    @RolesAllowed({"ProductManufacturer"})
    @DELETE
    @Path("/{id}")
    public Response deleteSensor(@PathParam("id") long id) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.find(id);

        if(sensor == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Sensor do not exist").build();

        boolean isDeleted = sensorBean.delete(id);
        if(!isDeleted)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        return Response.status(Response.Status.OK).entity("Sensor deleted").build();
    }

}
