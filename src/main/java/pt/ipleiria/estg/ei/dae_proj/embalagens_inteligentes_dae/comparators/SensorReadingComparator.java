package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.comparators;

import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.SensorReading;

import java.util.Comparator;

public class SensorReadingComparator implements Comparator<SensorReading> {

    @Override
    public int compare(SensorReading o1, SensorReading o2) {
        return Float.compare(o1.getValue(), o2.getValue());
    }
}
