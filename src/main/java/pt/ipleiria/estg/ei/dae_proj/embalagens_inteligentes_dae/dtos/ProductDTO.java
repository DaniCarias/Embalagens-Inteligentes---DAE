package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos;

public class ProductDTO {

    private long id;
    private String name;
    private String description;
    private String username_manufacturer;
    private long package_id;


    public ProductDTO() {
    }

    public ProductDTO(long id, String name, String description, String username_manufacturer, long package_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.username_manufacturer = username_manufacturer;
        this.package_id = package_id;
    }

    public ProductDTO(long id, String name, String description, String username_manufacturer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.username_manufacturer = username_manufacturer;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUsername_manufacturer() {
        return username_manufacturer;
    }
    public void setUsername_manufacturer(String username_manufacturer) {
        this.username_manufacturer = username_manufacturer;
    }
    public long getPackage_id() {
        return package_id;
    }
    public void setPackage_id(long package_id) {
        this.package_id = package_id;
    }
}
