package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.Date;
import java.util.LinkedList;
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
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.ProductManufacturerBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Authenticated;

@Path("packages")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class PackageService {

    @EJB
    private PackageBean packageBean;
    @EJB
    private ProductBean productBean;
    @EJB
    private OrderBean orderBean;
    @EJB
    private ProductManufacturerBean productManufacturerBean;

    private PackageDTO toDTO(Package _package) {
        var order_id = 0;
        var product_id = 0;

        if(_package.getOrder() != null){
            order_id = (int) _package.getOrder().getId();
        }

        if(_package.getProduct() != null){
            product_id = (int) _package.getProduct().getId();
        }

        return new PackageDTO(
                _package.getId(),
                _package.getPackageType(),
                _package.getLastTimeOpened(),
                _package.getMaterial(),
                order_id,
                product_id
        );

    }

    private List<PackageDTO> toDTOs(List<Package> packages) {
        return packages.stream().map(this::toDTO).collect(Collectors.toList());
    }


    private SensorDTO sensor_toDTO(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getName(),
                sensor.getPackage().getId()
        );
    }

    private List<SensorDTO> sensors_toDTOs(List<Sensor> sensors) {
        return sensors.stream().map(this::sensor_toDTO).collect(Collectors.toList());
    }

    @RolesAllowed({"LogisticOperator","ProductManufacturer"})
    @GET
    @Path("/")
    public Response getAllPackage() {
        return Response.status(Response.Status.OK).entity(toDTOs(packageBean.getAll())).build();
    }

    @RolesAllowed({"LogisticOperator","ProductManufacturer"})
    @GET
    @Path("/{id}")
    public Response getPackageDetails(@PathParam("id") long id) {
        Package _package = packageBean.find(id);
        if (_package != null) {
            return Response.status(Response.Status.OK).entity(toDTO(_package)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("The package do not exists").build();
    }

    @RolesAllowed({"ProductManufacturer"})
    @POST
    @Path("/")
    public Response createNewPackage(PackageDTO packageDTO) throws MyEntityNotFoundException, MyEntityExistsException{
        Package _package = packageBean.create(packageDTO.getPackageType(), packageDTO.getLastTimeOpened(), packageDTO.getMaterial());

        if(packageDTO.getOrder_id() > 0)
            packageBean.addOrder(_package.getId(), packageDTO.getOrder_id());

        if(packageDTO.getProduct_id() > 0){
            productBean.addPackage(packageDTO.getProduct_id(), _package.getId());
            packageBean.addProduct(_package.getId(), packageDTO.getProduct_id());
        }

        Package newPackage = packageBean.find(_package.getId());
        if(newPackage == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.status(Response.Status.CREATED).entity(toDTO(newPackage)).build();
    }

    @RolesAllowed({"ProductManufacturer"})
    @PUT
    @Path("/{id}")
    public Response editPackage(@PathParam("id") long id, PackageDTO packageDTO) throws MyEntityNotFoundException {
        /*Product product = productBean.find(packageDTO.getProduct_id());
        if(product == null)
            return Response.status(Response.Status.NOT_FOUND).build();*/

        packageBean.update(id, packageDTO.getPackageType(), packageDTO.getLastTimeOpened(), packageDTO.getMaterial()/*, product*/);

        /*if(packageDTO.getOrder_id() > 0)
            packageBean.addOrder(id, packageDTO.getOrder_id());
        else if(packageDTO.getOrder_id() < 0)
            packageBean.removeOrder(id);*/

        return Response.status(Response.Status.OK).entity("Package updated").build();
    }

    @RolesAllowed({"ProductManufacturer"})
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

    @RolesAllowed({"EndConsumer","ProductManufacturer"})
    @POST
    @Path("/{id}/order") //link order to package
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

    @RolesAllowed({"EndConsumer","ProductManufacturer"})
    @DELETE
    @Path("/{id}/order") //unlink order to package
    public Response removeOrder (@PathParam("id") long id) throws MyEntityNotFoundException {
        Package _package = packageBean.find(id);

        if(_package == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Package do not exists").build();

        if(_package.getOrder() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Package does not have a order").build();

        packageBean.removeOrder(id);

        return Response.status(Response.Status.OK).entity("Order removed").build();
    }

    @RolesAllowed({"ProductManufacturer"})
    @GET
    @Path("/manufacturer/{username}")
    public Response getPackagesByManufacturer(@PathParam("username") String username) {
        ProductManufacturer productManufacturer = productManufacturerBean.find(username);
        if(productManufacturer == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        List<Product> products = productBean.getAllByManufactor(username);
        if(products == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Do not have any products").build();

        List<Package> packages = packageBean.getPackagesByProducts(products);

        return Response.status(Response.Status.OK).entity(toDTOs(packages)).build();
    }

    @RolesAllowed({"LogisticOperator","ProductManufacturer"})
    @GET
    @Path("/{id}/sensors")
    public Response getSensorsByPackage(@PathParam("id") long id) throws MyEntityNotFoundException {
        Package pck = packageBean.getPackageSensors(id);
        if(pck == null)
            throw new MyEntityNotFoundException("Package with id: " + id + " not found");

        List<Sensor> sensors = pck.getSensors();
        if(sensors == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).entity(sensors_toDTOs(sensors)).build();
    }

    @RolesAllowed({"LogisticOperator", "EndConsumer","ProductManufacturer"})
    @GET
    @Path("/product/{id}")
    public Response getPackageByProduct(@PathParam("id") long id) throws MyEntityNotFoundException {
        Product product = productBean.find(id);
        if(product == null)
            throw new MyEntityNotFoundException("Product with id: " + id + " not found");

        Package _package = packageBean.getPackageByProduct(id);
        if(_package == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.status(Response.Status.OK).entity(toDTO(_package)).build();
    }
}
