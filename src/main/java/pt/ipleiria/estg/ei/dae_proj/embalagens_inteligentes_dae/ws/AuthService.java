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
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos.UserDTO;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs.UserBean;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Authenticated;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.TokenIssuer;


@Path("auth") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class AuthService {

    @Inject
    private TokenIssuer issuer;

    @Context
    private SecurityContext securityContext;

    @EJB
    private UserBean userBean;


    protected static final byte[] SECRET_KEY = "veRysup3rstr0nginv1ncible5ecretkeY@embalagens_inteligentes_dae.dae.ipleiria".getBytes();

    protected static final String ALGORITHM = "HMACSHA384";

    public static final long EXPIRY_MINS = 60L;


    @GET
    @Authenticated
    @Path("/user")
    public Response getAuthenticatedUser() {
        var username = securityContext.getUserPrincipal().getName();
        var user = userBean.findOrFail(username);
        return Response.ok(UserDTO.from(user)).build();
    }

    @POST
    @Path("/login")
    public Response authenticate(@Valid AuthDTO auth) {
        if (userBean.canLogin(auth.getUsername(), auth.getPassword())) {
            String token = issuer.issue(auth.getUsername());
            return Response.ok(token).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
