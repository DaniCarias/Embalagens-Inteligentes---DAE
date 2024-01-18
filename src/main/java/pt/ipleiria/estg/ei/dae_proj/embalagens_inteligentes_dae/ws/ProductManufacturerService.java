package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.LogisticsOperatorBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.OrderBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.ProductManufacturerBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Authenticated;

@Path("productmanufacturers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class ProductManufacturerService {

    @EJB
    private ProductManufacturerBean productManufacturerBean;
    @EJB
    private OrderBean orderBean;

    private ProductManufacturerDTO toDTO(ProductManufacturer productManufacturer) {
        return new ProductManufacturerDTO(
                productManufacturer.getUsername(),
                productManufacturer.getName(),
                productManufacturer.getPassword(),
                productManufacturer.getAddress(),
                productManufacturer.getPhoneNumber()
        );
    }

    @RolesAllowed({""})
    private List<ProductManufacturerDTO> toDTOs(List<ProductManufacturer> productManufacturers) {
        return productManufacturers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @RolesAllowed({""})
    @GET
    @Path("/")
    public List<ProductManufacturerDTO> getAllProductManufacturers() {
        return toDTOs(productManufacturerBean.getAll());
    }

    @RolesAllowed({""})
    @GET
    @Path("/{username}")
    public Response getProductManufacturerDetails(@PathParam("username") String username) {
        ProductManufacturer productManufacturer = productManufacturerBean.find(username);
        if (productManufacturer != null) {
            return Response.status(Response.Status.OK).entity(toDTO(productManufacturer)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Product Manufacturer do not exist").build();
    }

    @RolesAllowed({""})
    @POST
    @Path("/")
    public Response createNewProductManufacturer(ProductManufacturerDTO productManufacturerDTO) throws MyEntityNotFoundException{
        if(productManufacturerBean.exists(productManufacturerDTO.getUsername()))
            return Response.status(Response.Status.CONFLICT).build();

        ProductManufacturer productManufacturer = null;
        try {
            productManufacturer = productManufacturerBean.create(productManufacturerDTO.getUsername(),
                    productManufacturerDTO.getName(),productManufacturerDTO.getPassword(),
                    productManufacturerDTO.getAddress(), productManufacturerDTO.getPhoneNumber());
        } catch (MyEntityExistsException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Product manufacturer not created").build();
        }

        if(productManufacturer == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.status(Response.Status.CREATED).entity(toDTO(productManufacturer)).build();
    }

    @RolesAllowed({""})
    @PUT
    @Path("/{username}")
    public Response editProductManufacturer(@PathParam("username") String username,ProductManufacturerDTO productManu){
        ProductManufacturer productManufacturer = productManufacturerBean.find(username);

        if(productManufacturer == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Product Manufacturer not found").build();

        productManufacturerBean.update(username, productManu.getName(), productManu.getAddress(), productManu.getPhoneNumber());
        return Response.status(Response.Status.OK).entity(toDTO(productManufacturer)).entity("Consumer updated").build();
    }
    
    @RolesAllowed({""})
    @DELETE
    @Path("/{username}")
    public Response deleteProductManufacturer(@PathParam("username") String username) throws MyEntityNotFoundException {
        ProductManufacturer productManufacturer = productManufacturerBean.find(username);
        if(productManufacturer == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        boolean isDeleted = productManufacturerBean.delete(username);
        if(!isDeleted)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        return Response.status(Response.Status.OK).entity("End Consumer deleted").build();
    }

}