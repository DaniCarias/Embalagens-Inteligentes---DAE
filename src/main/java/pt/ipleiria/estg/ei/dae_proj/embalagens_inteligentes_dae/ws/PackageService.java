package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import java.util.Date;
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
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.ProductBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.*;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.Package;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.exceptions.MyEntityNotFoundException;

@Path("package")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PackageService {

    @EJB
    private PackageBean packageBean;

    @EJB
    private ProductBean productBean;

    private PackageDTO toDTO(Package _package) {
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

    @GET
    @Path("/")
    public List<PackageDTO> getAllPackage() {
        return toDTOs(packageBean.getAll());
    }

    @POST
    @Path("/")
    public Response createNewPackage(PackageDTO packageDTO) {
        Product product = productBean.find(packageDTO.getProduct_id());
        if(product == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        try {
            packageBean.create(
                    packageDTO.getPackageType(),
                    packageDTO.getLastTimeOpened(),
                    packageDTO.getMaterial(),
                    product
            );

        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Package newPackage = packageBean.find(packageDTO.getId());
        if(newPackage == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.status(Response.Status.CREATED).entity(toDTO(newPackage)).build();
    }

    @PUT
    @Path("/{id}")
    public Response editPackage(@PathParam("id") long id, PackageDTO packageDTO) throws MyEntityNotFoundException {
        Product product = productBean.find(packageDTO.getProduct_id());
        if(product == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        packageBean.update(
                id,
                packageDTO.getPackageType(),
                packageDTO.getLastTimeOpened(),
                packageDTO.getMaterial(),
                product
        );

        Package _package = packageBean.find(packageDTO.getId());
        if(_package == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).entity(toDTO(_package)).entity("package updated").build();
    }


    //TESTAR COM O "id" em string -> Pode nao converter automaticamente
    @DELETE
    @Path("/{id}")
    public Response deletePackage(@PathParam("id") long id) throws MyEntityNotFoundException {
        //long id = Long.parseLong(id);
        Package _package = packageBean.find(id);
        if(_package != null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Not possible to delete").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Package do not exist").build();
    }


}
