package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos;

public class SensorDTO {

    private long id;
    private String name;
    private long package_id;


    public SensorDTO() {
    }

    public SensorDTO(long id, String name, long package_id) {
        this.id = id;
        this.name = name;
        this.package_id = package_id;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getPackage_id() {
        return package_id;
    }

    public void setPackage_id(long package_id) {
        this.package_id = package_id;
    }
}
