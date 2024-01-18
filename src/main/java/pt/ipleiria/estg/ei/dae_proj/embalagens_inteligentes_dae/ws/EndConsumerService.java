package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.OrderDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.EndConsumerBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.OrderBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.PackageBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

@Path("endconsumers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class EndConsumerService {

    @EJB
    private EndConsumerBean endConsumerBean;
    @EJB
    private OrderBean orderBean;

    private EndConsumerDTO toDTO(EndConsumer endconsumer) {
        return new EndConsumerDTO(
                endconsumer.getUsername(),
                endconsumer.getName(),
                endconsumer.getPassword(),
                endconsumer.getAddress(),
                endconsumer.getPhoneNumber()
        );
    }
    private List<EndConsumerDTO> toDTOs(List<EndConsumer> endConsumer) {
        return endConsumer.stream().map(this::toDTO).collect(Collectors.toList());
    }


    private OrderDTO order_toDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getEndConsumer().getUsername()
        );
    }
    private List<OrderDTO> orders_toDTOs(List<Order> orders) {
        return orders.stream().map(this::order_toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<EndConsumerDTO> getAllConsumers() {
        return toDTOs(endConsumerBean.getAll());
    }

    @GET
    @Path("/{username}")
    public Response getEndConsumerDetails(@PathParam("username") String username) {
        EndConsumer endConsumer = endConsumerBean.find(username);
        if (endConsumer != null) {
            return Response.status(Response.Status.OK).entity(toDTO(endConsumer)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("End Consumer do not exist").build();
    }

    @POST
    @Path("/")
    public Response createNewEndConsumer(EndConsumerDTO endConsumerDTO) throws MyEntityExistsException{

            if(endConsumerBean.exists(endConsumerDTO.getUsername()))
                return Response.status(Response.Status.CONFLICT).build();

            EndConsumer endConsumer = endConsumerBean.create(endConsumerDTO.getUsername(),endConsumerDTO.getName(),endConsumerDTO.getPassword(), endConsumerDTO.getAddress(), endConsumerDTO.getPhoneNumber());

            if(endConsumer == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            return Response.status(Response.Status.CREATED).entity(toDTO(endConsumer)).build();
    }

    @PUT
    @Path("/{username}")
    public Response editEndConsumer(@PathParam("username") String username, EndConsumerDTO endCons){
        EndConsumer endConsumer = endConsumerBean.find(username);

        if(endConsumer == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Consumer not found").build();

        endConsumerBean.update(username, endCons.getName(), endCons.getAddress(), endCons.getPhoneNumber());
        return Response.status(Response.Status.OK).entity(toDTO(endConsumer)).entity("Consumer updated").build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteEndConsumer(@PathParam("username") String username) throws MyEntityNotFoundException {
        EndConsumer endconsumer = endConsumerBean.find(username);
        if(endconsumer == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        boolean isDeleted = endConsumerBean.delete(username);
        if(!isDeleted)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        return Response.status(Response.Status.OK).entity("End Consumer deleted").build();
    }

    @GET
    @Path("/{username}/orders")
    public Response getEndConsumerOrders(@PathParam("username") String username) throws MyEntityNotFoundException{

        EndConsumer endConsumer = endConsumerBean.getEndConsumerOrders(username);

        if(endConsumer == null)
            throw new MyEntityNotFoundException("End Consumer with username: " + username + " not found");

        var orders = orders_toDTOs(endConsumer.getOrders());

        return Response.status(Response.Status.OK).entity(orders).build();

    }
}