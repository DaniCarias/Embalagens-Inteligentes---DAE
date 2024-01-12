package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos;

public class OrderDTO {

    private long id;
    private String endConsmer_username;


    public OrderDTO() {
    }

    public OrderDTO(long id, String endConsmer_username) {
        this.id = id;
        this.endConsmer_username = endConsmer_username;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getEndConsmer_username() {
        return endConsmer_username;
    }
    public void setEndConsmer_username(String endConsmer_username) {
        this.endConsmer_username = endConsmer_username;
    }
}
