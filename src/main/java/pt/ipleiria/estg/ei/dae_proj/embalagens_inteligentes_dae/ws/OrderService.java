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
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

@Path("orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderService {

    @EJB
    private OrderBean orderBean;
    @EJB
    private EndConsumerBean endConsumerBean;
    @EJB
    private PackageBean packageBean;

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

    @GET
    @Path("/{id}")
    public Response getOrderDetails(@PathParam("id") long id) {
        Order order = orderBean.find(id);
        if (order != null) {
            return Response.status(Response.Status.OK).entity(toDTO(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Order do not exist").build();
    }

    @POST
    @Path("/")
    public Response createNewOrder(OrderDTO orderDTO) {
        try {
            EndConsumer consumer = endConsumerBean.find(orderDTO.getEndConsumer_username());
            if(consumer == null)
                return Response.status(Response.Status.NOT_FOUND).entity("Consumer not found").build();

            Order order = orderBean.create(consumer);

            if(order == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            return Response.status(Response.Status.CREATED).entity(toDTO(order)).build();

        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response editOrder(@PathParam("id") long id, OrderDTO orderDTO) throws MyEntityNotFoundException {
        EndConsumer endConsumer = endConsumerBean.find(orderDTO.getEndConsumer_username());
        Order order = orderBean.find(id);

        if(endConsumer == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Consumer not found").build();

        if(order == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Order not found").build();

        orderBean.update(id, endConsumer);
        return Response.status(Response.Status.OK).entity(toDTO(order)).entity("Order updated").build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") long id) throws MyEntityNotFoundException {
        Order order = orderBean.find(id);
        if(order == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        boolean isDeleted = orderBean.delete(id);
        if(!isDeleted)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        return Response.status(Response.Status.OK).entity("Package deleted").build();
    }





    @POST
    @Path("/{id}/addpackage")
    public Response addPackage(@PathParam("id") long id, long package_id) throws MyEntityNotFoundException {
        Order order = orderBean.find(id);
        Package _package = packageBean.find(package_id);

        if(order == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Order not found").build();

        if(_package == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Package not found").build();

        orderBean.addPackage(id, package_id);
        return Response.status(Response.Status.OK).entity("Package added to order").build();
    }



}
