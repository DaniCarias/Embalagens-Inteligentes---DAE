package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.SensorReadingDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.PackageBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.ProductBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.SensorBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.SensorReadingBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Product;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Sensor;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.SensorReading;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("sensor_readings")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"LogisticOperator", "ProductManufacturer", "EndConsumer"})
public class SensorReadingService {

    @EJB
    private SensorReadingBean sensorReadingBean;

    @EJB
    private SensorBean sensorBean;
    @EJB
    private ProductBean productBean;

    @EJB
    private PackageBean packageBean;

    private SensorReadingDTO toDTO(SensorReading sensorReading) {
        return new SensorReadingDTO(
                sensorReading.getId(),
                sensorReading.getDate(),
                sensorReading.getValue(),
                sensorReading.getSensor().getId(),
                sensorReading.doesViolateQualityConstraint()
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
    public Response addNewSensorReading(SensorReadingDTO sensorReadingDTO) throws MyEntityNotFoundException {
        Sensor sensor = sensorBean.find(sensorReadingDTO.getSensor_id());
        if(sensor == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor not found").build();

        SensorReading sensorReading = sensorReadingBean.create(sensorReadingDTO.getValue(), sensorReadingDTO.getSensor_id());
        if(sensorReading == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Fail to create").build();

        return Response.status(Response.Status.CREATED).entity(toDTO(sensorReading)).build();
    }

    @GET
    @Path("/sensor/{sensor_id}")
    public Response getSensorReadingsForSensor(@PathParam("sensor_id") long sensor_id) {
        Sensor sensor = sensorBean.find(sensor_id);
        if(sensor == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor not found").build();
        }

        return Response.status(Response.Status.OK).entity(toDTOs(sensorReadingBean.getSensorReadingsForSensor(sensor_id))).build();
    }

    @GET
    @Path("/product/{product_id}")
    public Response getSensorReadingsForProduct(@PathParam("product_id") long product_id, @QueryParam("violating") boolean violating) {
        Product product = productBean.find(product_id);
        if(product == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Product not found").build();
        }

        return Response.status(Response.Status.OK).entity(toDTOs(sensorReadingBean.getSensorReadingsForProduct(product_id, violating))).build();
    }

    @GET
    @Path("/package/{package_id}")
    public Response getSensorReadingsForPackage(@PathParam("package_id") long package_id) {
        Package _package = packageBean.find(package_id);
        if(_package == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("PACKAGE_NOT_FOUND").build();
        }

        return Response.status(Response.Status.OK).entity(toDTOs(sensorReadingBean.getSensorReadingsForPackage(package_id))).build();
    }


}
