package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ws;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.AuthDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.AuthResponseDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.SensorReadingDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.UserDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.SensorReadingBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.UserBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.AuthResponse;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.SensorReading;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Authenticated;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.TokenIssuer;


@Path("auth")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AuthService {

    @Inject
    private TokenIssuer issuer;

    @Context
    private SecurityContext securityContext;

    @EJB
    private UserBean userBean;



    private AuthResponseDTO toDTO(AuthResponse authResponse) {
        return new AuthResponseDTO(
                authResponse.getToken(),
                authResponse.getUser_type()
        );
    }


    @POST
    @Path("/login")
    public Response authenticate(@Valid AuthDTO auth) {
        if (userBean.canLogin(auth.getUsername(), auth.getPassword())) {

            String token = issuer.issue(auth.getUsername());
            String user_type = userBean.find(auth.getUsername()).getClass().getSimpleName();

            AuthResponse authResponse = new AuthResponse(token, user_type);

            return Response.status(Response.Status.OK).entity(toDTO(authResponse)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
