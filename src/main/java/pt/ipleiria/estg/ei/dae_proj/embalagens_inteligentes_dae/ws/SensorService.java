package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.OrderDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.SensorBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorService {

    @EJB
    private SensorBean SensorBean;
    @EJB
    private PackageBean packageBean;

    private SensorDTO toDTO(Sensor sensor) {

        return new SensorDTO(
                sensor.getId();
                sensor.getName();
        );
    }

    private List<SensorDTO> toDTOs(List<Sensor> sensors) {
        return sensors.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<SensorDTO> getAllSensors() {
        return toDTOs(sensorBean.getAll());
    }

    @POST
    @Path("/")
    public Response createNewSensor(SensorDTO sensorDTO) {
        Package pck = packageBean.find(sensorDTO.getPackage());
        if(pck == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        try {
            Sensor sensor = sensorBean.create(sensorDTO.getName().pck.getId());

            if(sensor == null)
                return Response.status(Response.Status.BAD_REQUEST).build();
            sensor = sensorBean.find(sensor.getId());

            return Response.status(Response.Status.CREATED).entity(toDTO(sensor)).build();
        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response editSensor(@PathParam("id") long id, SensorDTO sensorDTO) throws MyEntityNotFoundException {
        Package pck = packageBean.find(sensorDTO.getPackage());
        if(pck == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        sensorBean.update(
                sensorDTO.getName(),
                pck.getId()
        );

        Sensor sensor = sensorBean.find(sensorDTO.getId());
        if(sensor == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).entity(toDTO(product)).entity("Sensor updated").build();
    }


    //TESTAR COM O "id" em string -> Pode nao converter automaticamente
    @DELETE
    @Path("/{id}")
    public Response deleteSensor(@PathParam("id") long id) throws MyEntityNotFoundException {
        //long id = Long.parseLong(id);
        Sensor sensor = sensorBean.find(id);

        if(sensor == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Sensor do not exist").build();

        boolean isDeleted = sensorBean.delete(id);
        if(!isDeleted)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        return Response.status(Response.Status.OK).entity("Sensor deleted").build();
    }
}
