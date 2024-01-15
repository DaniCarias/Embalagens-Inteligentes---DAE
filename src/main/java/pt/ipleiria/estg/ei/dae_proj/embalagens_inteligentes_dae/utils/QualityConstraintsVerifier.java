package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.utils;

import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.QualityConstraint;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.SensorReading;

import java.util.List;

// utilities related to quality constraints verification
public class QualityConstraintsVerifier {

    public static boolean verifyConstraintOnReading(QualityConstraint constraint, SensorReading reading) throws Exception {

        switch(constraint.getType()) {
            case LOWER:
                return reading.getValue() <= constraint.getValue();
            case UPPER:
                return reading.getValue() >= constraint.getValue();
            case EXACT:
                return Float.compare(reading.getValue(), constraint.getValue()) == 0;
        }
        throw new Exception("Invalid constraint type!");
    }

    public static boolean verifyConstraintListOnReading(List<QualityConstraint> constraints, SensorReading reading)
            throws Exception {

        // iterate over the constraints
        for(var c : constraints) {

            // return false on the first violating the constraint
            if(!verifyConstraintOnReading(c, reading))
                return false;
        }

        // if execution reaches here, no constraints were violated
        return true;
    }
}
