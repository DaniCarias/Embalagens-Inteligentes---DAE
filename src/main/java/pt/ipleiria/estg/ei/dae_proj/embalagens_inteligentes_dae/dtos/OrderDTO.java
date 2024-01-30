package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos;

public class OrderDTO {

    private long id;
    private String endConsumer_username;


    public OrderDTO() {
    }

    public OrderDTO(long id, String endConsumer_username) {
        this.id = id;
        this.endConsumer_username = endConsumer_username;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getEndConsumer_username() {
        return endConsumer_username;
    }
    public void setEndConsumer_username(String endConsumer_username) {
        this.endConsumer_username = endConsumer_username;
    }
}
