package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.OrderDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.OrderBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.PackageBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.ProductBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

@Path("packages")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed({"ProductManufacturer"})
public class PackageService {

    @EJB
    private PackageBean packageBean;
    @EJB
    private ProductBean productBean;
    @EJB
    private OrderBean orderBean;

    private PackageDTO toDTO(Package _package) {
        if (_package.getOrder() == null){
            return new PackageDTO(
                    _package.getId(),
                    _package.getPackageType(),
                    _package.getLastTimeOpened(),
                    _package.getMaterial(),
                    _package.getProduct().getId()
            );
        }

        return new PackageDTO(
                _package.getId(),
                _package.getPackageType(),
                _package.getLastTimeOpened(),
                _package.getMaterial(),
                _package.getProduct().getId(),
                _package.getOrder().getId()
        );
    }

    private List<PackageDTO> toDTOs(List<Package> packages) {
        return packages.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @RolesAllowed({"LogisticOperator"})
    @GET
    @Path("/")
    public List<PackageDTO> getAllPackage() {
        return toDTOs(packageBean.getAll());
    }

    @RolesAllowed({"LogisticOperator"})
    @GET
    @Path("/{id}")
    public Response getPackageDetails(@PathParam("id") long id) {
        Package _package = packageBean.find(id);
        if (_package != null) {
            return Response.status(Response.Status.OK).entity(toDTO(_package)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("The package do not exists").build();
    }

    @POST
    @Path("/")
    public Response createNewPackage(PackageDTO packageDTO) throws MyEntityNotFoundException, MyEntityExistsException{
        Product product = productBean.find(packageDTO.getProduct_id());
        if(product == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        Package _package = packageBean.create(packageDTO.getPackageType(), packageDTO.getLastTimeOpened(), packageDTO.getMaterial(), product);

        if(packageDTO.getOrder_id() > 0)
            packageBean.addOrder(_package.getId(), packageDTO.getOrder_id());

        Package newPackage = packageBean.find(_package.getId());
        if(newPackage == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.status(Response.Status.CREATED).entity(toDTO(newPackage)).build();
    }

    @PUT
    @Path("/{id}")
    public Response editPackage(@PathParam("id") long id, PackageDTO packageDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        Product product = productBean.find(packageDTO.getProduct_id());
        if(product == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        packageBean.update(id, packageDTO.getPackageType(), packageDTO.getLastTimeOpened(), packageDTO.getMaterial(), product);

        if(packageDTO.getOrder_id() > 0)
            packageBean.addOrder(id, packageDTO.getOrder_id());
        else
            packageBean.removeOrder(id);

        return Response.status(Response.Status.OK).entity("Package updated").build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePackage(@PathParam("id") long id) throws MyEntityNotFoundException {
        Package _package = packageBean.find(id);
        if(_package == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Package do not exist").build();

        boolean isDeleted = packageBean.delete(id);
        if(!isDeleted)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        return Response.status(Response.Status.OK).entity("Package deleted").build();
    }

    @POST
    @Path("/{id}/order")
    public Response addOrder(@PathParam("id") long id, OrderDTO orderDTO) throws MyEntityNotFoundException, MyEntityExistsException {
        Package _package = packageBean.find(id);
        Order order = orderBean.find(orderDTO.getId());

        if(_package == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Package do not exists").build();

        if(order == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Order do not exists").build();

        if(_package.getOrder() != null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Package already has an order").build();

        packageBean.addOrder(id, orderDTO.getId());

        return Response.status(Response.Status.OK).entity("Order added").build();
    }

    @DELETE
    @Path("/{id}/order")
    public Response removeOrder (@PathParam("id") long id) throws MyEntityNotFoundException {
        Package _package = packageBean.find(id);

        if(_package == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Package do not exists").build();

        if(_package.getOrder() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Package does not have a order").build();

        packageBean.removeOrder(id);

        return Response.status(Response.Status.OK).entity("Order removed").build();
    }

    //@RolesAllowed({"ProductManufacturer"})
    //TODO: LISTAR SO OS PACKAGES DO MANUFACTURER

    //@RolesAllowed({"LogisticOperator"})
    //TODO: LISTAR PACKAGES DO LOGISTIC OPERATOR

    //@RolesAllowed({"LogisticOperator"})
    //TODO: LISTAR SENSORES DO PACKAGE
}
