package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.OrderDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.EndConsumerBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.LogisticsOperatorBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.OrderBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.PackageBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

@Path("logisticoperator")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class LogisticOperatorService {

    @EJB
    private LogisticsOperatorBean logisticsOperatorBean;
    @EJB
    private OrderBean orderBean;

    private LogisticsOperatorDTO toDTO(LogisticsOperator logisticsOperator) {
        return new LogisticsOperatorDTO(
                logisticsOperator.getUsername(),
                logisticsOperator.getName(),
                logisticsOperator.getPassword(),
                logisticsOperator.getAddress(),
                logisticsOperator.getPhoneNumber()
        );
    }


    private List<LogisticsOperatorDTO> toDTOs(List<LogisticsOperator> logisticsOperators) {
        return logisticsOperators.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<LogisticsOperatorDTO> getAllLogisticsOperators() {
        return toDTOs(logisticsOperatorBean.getAll());
    }

    @GET
    @Path("/{username}")
    public Response getLogisticsOperatorDetails(@PathParam("username") String username) {
        LogisticsOperator logisticsOperator = logisticsOperatorBean.find(username);
        if (logisticsOperator != null) {
            return Response.status(Response.Status.OK).entity(toDTO(logisticsOperator)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Logistics Operator do not exist").build();
    }

    @POST
    @Path("/")
    public Response createNewLogisticsOperator(LogisticsOperatorDTO logisticsOperatorDTO) {
            if(logisticsOperatorBean.exists(logisticsOperatorDTO.getUsername()))
                return Response.status(Response.Status.CONFLICT).build();

        LogisticsOperator logisticsOperator = null;
        try {
            logisticsOperator = logisticsOperatorBean.create(logisticsOperatorDTO.getUsername(),
                    logisticsOperatorDTO.getName(),logisticsOperatorDTO.getPassword(),
                    logisticsOperatorDTO.getAddress(), logisticsOperatorDTO.getPhoneNumber());
        } catch (MyEntityExistsException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Logistics operator not created.").build();
        }

        if(logisticsOperator == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            return Response.status(Response.Status.CREATED).entity(toDTO(logisticsOperator)).build();

    }

    @PUT
    @Path("/{username}")
    public Response editEndLogisticsOperator(@PathParam("username") String username,LogisticsOperatorDTO logisticsOper) throws MyEntityNotFoundException {
        LogisticsOperator logisticsOperator = logisticsOperatorBean.find(username);

        if(logisticsOperator == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Consumer not found").build();

        logisticsOperatorBean.update(username,logisticsOper.getName(),logisticsOper.getPassword(), logisticsOper.getAddress(), logisticsOper.getPhoneNumber());
        return Response.status(Response.Status.OK).entity(toDTO(logisticsOperator)).entity("Consumer updated").build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteEndLogisticsOperator(@PathParam("username") String username) throws MyEntityNotFoundException {
        LogisticsOperator logisticsOperator = logisticsOperatorBean.find(username);
        if(logisticsOperator == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        boolean isDeleted = logisticsOperatorBean.delete(username);
        if(!isDeleted)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        return Response.status(Response.Status.OK).entity("End Consumer deleted").build();
    }

   /* @GET
    @Path("/{username}/orders")
    public Response getEndConsumerOrders(@PathParam("username") String username) throws MyEntityNotFoundException{
        Order List<Order> = orderBean.
        return Response.status(Response.Status.OK).entity("End Consumer do not exist").build();
    }*/
}