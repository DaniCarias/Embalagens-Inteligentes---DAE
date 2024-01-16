package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.OrderDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.ProductDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.ProductBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.ProductManufacturerBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

@Path("products")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProductService {

    @EJB
    private ProductBean productBean;
    @EJB
    private ProductManufacturerBean productManufacturerBean;

    private ProductDTO toDTO(Product product) {
        if (product.getPackage() == null){
            return new ProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getProductManufacturer().getUsername()
                    );
        }

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getProductManufacturer().getUsername(),
                product.getPackage().getId()
        );
    }

    private List<ProductDTO> toDTOs(List<Product> products) {
        return products.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<ProductDTO> getAllProducts() {
        return toDTOs(productBean.getAll());
    }

    @POST
    @Path("/")
    public Response createNewProduct(ProductDTO productDTO) {
        ProductManufacturer productManufacturer = productManufacturerBean.find(productDTO.getUsername_manufacturer());
        if(productManufacturer == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        try {
            Product product = productBean.create(productDTO.getName(), productDTO.getDescription(), productManufacturer);

            if(product == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            if(productDTO.getPackage_id() > 0)
                productBean.addPackage(product.getId(), productDTO.getPackage_id());

            product = productBean.find(product.getId());

            return Response.status(Response.Status.CREATED).entity(toDTO(product)).build();
        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response editProduct(@PathParam("id") long id, ProductDTO productDTO) throws MyEntityNotFoundException {
        ProductManufacturer productManufacturer = productManufacturerBean.find(productDTO.getUsername_manufacturer());
        if(productManufacturer == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        productBean.update(
                id,
                productDTO.getName(),
                productDTO.getDescription(),
                productManufacturer
        );

        Product product = productBean.find(productDTO.getId());
        if(product == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).entity(toDTO(product)).entity("Product updated").build();
    }


    //TESTAR COM O "id" em string -> Pode nao converter automaticamente
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") long id) throws MyEntityNotFoundException {
        //long id = Long.parseLong(id);
        Product product = productBean.find(id);
        if(product != null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Product do not exist").build();
    }
}
