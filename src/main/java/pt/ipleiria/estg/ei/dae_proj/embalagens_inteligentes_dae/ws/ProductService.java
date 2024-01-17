package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.OrderDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.PackageDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.ProductDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.PackageBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.ProductBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.ProductManufacturerBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

@Path("products")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProductService {

    @EJB
    private ProductBean productBean;
    @EJB
    private ProductManufacturerBean productManufacturerBean;
    @EJB
    private PackageBean packageBean;

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
                productManufacturer,
                productDTO.getPackage_id()
        );

        Product product = productBean.find(id);
        if(product == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).entity("Product updated").build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") long id) throws MyEntityNotFoundException {
        Product product = productBean.find(id);

        if(product == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Product do not exist").build();

        boolean isDeleted = productBean.delete(id);
        if(!isDeleted)
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();

        return Response.status(Response.Status.OK).entity("Product deleted").build();
    }

    @POST
    @Path("/{id}/addpackage")
    public Response addPackage(@PathParam("id") long id, long package_id) throws MyEntityNotFoundException {
        Product product = productBean.find(id);
        if(product == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        Package _package = packageBean.find(package_id);
        if(_package == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Package do not exists").build();

        if(product.getPackage() != null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Product already has a package").build();

        productBean.addPackage(id, package_id);

        product = productBean.find(id);
        return Response.status(Response.Status.OK).entity(product).entity("Package added").build();
    }

    @POST
    @Path("/{id}/removepackage")
    public Response removePackage(@PathParam("id") long id) throws MyEntityNotFoundException {
        Product product = productBean.find(id);
        if(product == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        if(product.getPackage() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Product does not have a package").build();

        productBean.removePackage(id);

        product = productBean.find(id);
        return Response.status(Response.Status.OK).entity(product).entity("Package removed").build();
    }

    @GET
    @Path("/{id}")
    public Response getProductDetails(@PathParam("id") long id) {
        Product product = productBean.find(id);
        if (product != null) {
            return Response.ok(toDTO(product)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_PRODUCT").build();
    }




}
