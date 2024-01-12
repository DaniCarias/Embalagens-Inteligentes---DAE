package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.dtos;

import java.util.Date;

public class SensorReadingDTO {

    private long id;
    private Date date;
    private float value;
    private long sensor_id;


    public SensorReadingDTO() {
    }

    public SensorReadingDTO(long id, Date date, float value, long sensor_id) {
        this.id = id;
        this.date = date;
        this.value = value;
        this.sensor_id = sensor_id;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
    public long getSensor_id() {
        return sensor_id;
    }
    public void setSensor_id(long sensor_id) {
        this.sensor_id = sensor_id;
    }
}
