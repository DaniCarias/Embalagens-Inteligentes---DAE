package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos;

public class AuthResponseDTO {

    String token;
    String user_type;


    public AuthResponseDTO() {
    }
    public AuthResponseDTO(String token, String user_type) {
        this.token = token;
        this.user_type = user_type;
    }


    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUser_type() {
        return user_type;
    }
    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
