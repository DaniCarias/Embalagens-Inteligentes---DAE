package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.QualityConstraintDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.SensorDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.QualityConstraintBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.QualityConstraint;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("quality_constraints")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class QualityConstraintService {

    @EJB
    private QualityConstraintBean qualityConstraintBean;

    private QualityConstraintDTO toDTO(QualityConstraint qualityConstraint) {
        if(qualityConstraint.getSensor() == null) {
            return new QualityConstraintDTO(
                    qualityConstraint.getId(),
                    qualityConstraint.getValue(),
                    qualityConstraint.getType(),
                    qualityConstraint.getProduct().getId()
            );
        }
        return new QualityConstraintDTO(
                qualityConstraint.getId(),
                qualityConstraint.getValue(),
                qualityConstraint.getType(),
                qualityConstraint.getProduct().getId(),
                qualityConstraint.getSensor().getId()
        );
    }

    private List<QualityConstraintDTO> toDTOs(List<QualityConstraint> qualityConstraints) {
        return qualityConstraints.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    //@RolesAllowed({""})
    public List<QualityConstraintDTO> getAllQualityConstraints() {
        return toDTOs(qualityConstraintBean.getAllConstraints());
    }

    @GET
    @Path("/{constraint_id}")
    //@RolesAllowed({""})
    public Response getQualityConstraint(@PathParam("constraint_id") long constraint_id) {
        QualityConstraint constraint = qualityConstraintBean.find(constraint_id);
        if(constraint != null) {
            return Response.ok(toDTO(constraint)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("CONSTRAINT_NOT_FOUND").build();
    }

    @GET
    @Path("/product/{product_id}")
    @RolesAllowed({"ProductManufacturer"})
    public List<QualityConstraintDTO> getAllQualityConstraintsForProduct(@PathParam("product_id") long product_id) {
        return toDTOs(qualityConstraintBean.getAllConstraintsForProduct(product_id));
    }

    @GET
    @Path("/sensor/{sensor_id}")
    @RolesAllowed({"LogisticsOperator"})
    public List<QualityConstraintDTO> getAllQualityConstraintsForSensor(@PathParam("sensor_id") long sensor_id) {
        return toDTOs(qualityConstraintBean.getAllConstraintsForSensor(sensor_id));
    }

    @GET
    @Path("/order/{order_id}")
    @RolesAllowed({"LogisticsOperator", "EndConsumer"})
    public List<QualityConstraintDTO> getAllQualityConstraintsForOrderId(@PathParam("order_id") long order_id) {
        return toDTOs(qualityConstraintBean.getAllConstraintsForOrder(order_id));
    }

    @GET
    @Path("/package/{package_id}")
    @RolesAllowed({"LogisticsOperator", "EndConsumer"})
    public List<QualityConstraintDTO> getAllQualityConstraintsForPackageId(@PathParam("package_id") long package_id) {
        return toDTOs(qualityConstraintBean.getAllConstraintsForPackage(package_id));
    }

    @POST
    @Path("/")
    @RolesAllowed({"ProductManufacturer"})
    public Response addNewQualityConstraint(QualityConstraintDTO qualityConstraintDTO) {
        try {
            QualityConstraint qualityConstraint = qualityConstraintBean.create(
                    qualityConstraintDTO.getValue(),
                    qualityConstraintDTO.getType(),
                    qualityConstraintDTO.getProduct_id()
            );
            if(qualityConstraint == null)
                return Response.status(Response.Status.NOT_FOUND).build();

            return Response.status(Response.Status.CREATED).entity(toDTO(qualityConstraint)).build();
        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{quality_constraint_id}/sensor")
    @RolesAllowed({"LogisticsOperator"})
    public Response setSensor(@PathParam("quality_constraint_id") long quality_constraint_id, SensorDTO sensorDTO) {

        try {
            qualityConstraintBean.setSensor(quality_constraint_id, sensorDTO.getId());
            return Response.status(Response.Status.OK).entity("SENSOR_ADDED").build();
        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{quality_constraint_id}")
    @RolesAllowed({"ProductManufacturer"})
    public Response updateQualityConstraint(@PathParam("quality_constraint_id") long quality_constraint_id,
                                            QualityConstraintDTO qualityConstraintDTO) {
        try {
            qualityConstraintBean.update(quality_constraint_id, qualityConstraintDTO.getValue(),
                    qualityConstraintDTO.getType(), qualityConstraintDTO.getSensor_id());
            return Response.status(Response.Status.OK).entity("Quality constraint updated successfully.").build();
        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{quality_constraint_id}")
    @RolesAllowed({"ProductManufacturer"})
    public Response deleteQualityConstraint(@PathParam("quality_constraint_id") long quality_constraint_id) {
        try {
            qualityConstraintBean.delete(quality_constraint_id);

            return Response.status(Response.Status.OK).entity("Quality constraint deleted successfully.").build();
        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }


}
