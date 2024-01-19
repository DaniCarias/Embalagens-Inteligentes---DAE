package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos;

import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.QualityConstraint;

public class QualityConstraintDTO {

    private long id;
    private float value;
    private QualityConstraint.ConstraintType type;
    private long product_id;
    private long sensor_id;

    public QualityConstraintDTO() {

    }

    public QualityConstraintDTO(long id, float value, QualityConstraint.ConstraintType type, long product_id, long sensor_id) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.product_id = product_id;
        this.sensor_id = sensor_id;
    }

    public QualityConstraintDTO(long id, float value, QualityConstraint.ConstraintType type, long product_id) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.product_id = product_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public QualityConstraint.ConstraintType getType() {
        return type;
    }

    public void setType(QualityConstraint.ConstraintType type) {
        this.type = type;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public long getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(long sensor_id) {
        this.sensor_id = sensor_id;
    }
}
