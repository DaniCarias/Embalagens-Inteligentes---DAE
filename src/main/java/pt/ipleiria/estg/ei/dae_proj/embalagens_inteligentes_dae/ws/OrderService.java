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
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

@Path("orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderService {

    @EJB
    private OrderBean orderBean;
    @EJB
    private EndConsumerBean endConsumerBean;

    private OrderDTO toDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getEndConsumer().getUsername()
        );
    }

    private List<OrderDTO> toDTOs(List<Order> orders) {
        return orders.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<OrderDTO> getAllOrders() {
        return toDTOs(orderBean.getAll());
    }

    @POST
    @Path("/")
    public Response createNewOrder(OrderDTO orderDTO) {
        EndConsumer consumer = endConsumerBean.find(orderDTO.getEndConsmer_username());
        if(consumer == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        try {
            orderBean.create(consumer);
        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Order newOrder = orderBean.find(orderDTO.getId());
        if(newOrder == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.status(Response.Status.CREATED).entity(toDTO(newOrder)).build();
    }

    @PUT
    @Path("/{id}")
    public Response editOrder(@PathParam("id") long id, OrderDTO orderDTO) throws MyEntityNotFoundException {
        EndConsumer endConsumer = endConsumerBean.find(orderDTO.getEndConsmer_username());
        if(endConsumer == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        orderBean.update(
                id,
                endConsumer
        );

        Order order = orderBean.find(orderDTO.getId());
        if(order == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).entity(toDTO(order)).entity("Order updated").build();
    }


    //TESTAR COM O "id" em string -> Pode nao converter automaticamente
    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") long id) throws MyEntityNotFoundException {
        //long id = Long.parseLong(id);
        Order order = orderBean.find(id);
        if(order != null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Order do not exist").build();
    }
}
