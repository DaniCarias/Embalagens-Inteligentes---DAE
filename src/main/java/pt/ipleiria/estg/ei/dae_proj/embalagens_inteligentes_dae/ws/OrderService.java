package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.security.RolesAllowed;
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
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Authenticated;

@Path("orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
//@Authenticated
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

    private PackageDTO package_toDTO(Package _package) {
        return new PackageDTO(
                _package.getId(),
                _package.getPackageType(),
                _package.getLastTimeOpened(),
                _package.getMaterial(),
                _package.getProduct().getId()
        );
    }

    private List<PackageDTO> packages_toDTOs(List<Package> packages) {
        return packages.stream().map(this::package_toDTO).collect(Collectors.toList());
    }

    @RolesAllowed({"LogisticOperator"})
    @GET
    @Path("/")
    public Response getAllOrders() {
        return Response.status(Response.Status.OK).entity(toDTOs(orderBean.getAll())).build();
    }

    @RolesAllowed({"EndConsumer"})
    @GET
    @Path("/endconsumer/{username}")
    public Response getOrdersFromEndConsumer(@PathParam("username") String username) {
        EndConsumer endConsumer = endConsumerBean.find(username);
        if(endConsumer == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Consumer not found").build();

        List<Order> orders = orderBean.getOrderByEndConsumer(username);

        return Response.status(Response.Status.OK).entity(toDTOs(orders)).build();
    }

    @RolesAllowed({"LogisticOperator", "EndConsumer"})
    @GET
    @Path("/{id}")
    public Response getOrderDetails(@PathParam("id") long id) {
        Order order = orderBean.find(id);
        if (order != null) {
            return Response.status(Response.Status.OK).entity(toDTO(order)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Order do not exist").build();
    }

    @RolesAllowed({"EndConsumer"})
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

    @RolesAllowed({"LogisticOperator"})
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

    @RolesAllowed({"EndConsumer"}) //CANCELAR Order
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

    @RolesAllowed({"EndConsumer"})
    @GET
    @Path("/{id}/packages")
    public Response getPackages(@PathParam("id") long id) throws MyEntityNotFoundException {
        Order order = orderBean.getOrderPackages(id);

        if(order == null)
            throw new MyEntityNotFoundException("Order with id: " + id + " not found");

        var packages = packages_toDTOs(order.getPackages());

        return Response.status(Response.Status.OK).entity(packages).build();
    }

    @RolesAllowed({"EndConsumer"})
    @GET
    @Path("/{id}/endconsumer/{username}")
    public Response getOrderByIdByEndConsumer(@PathParam("id") long id, @PathParam("username") String username) throws MyEntityNotFoundException {
        Order order = orderBean.find(id);
        if(order == null)
            throw new MyEntityNotFoundException("Order with id: " + id + " not found");

        EndConsumer endConsumer = endConsumerBean.find(username);
        if(endConsumer == null)
            throw new MyEntityNotFoundException("End Consumer with username: " + username + " not found");

        List<Order> orders = orderBean.getOrderByIdByEndConsumer(username, id);

        return Response.status(Response.Status.OK).entity(toDTOs(orders)).build();
    }
}
