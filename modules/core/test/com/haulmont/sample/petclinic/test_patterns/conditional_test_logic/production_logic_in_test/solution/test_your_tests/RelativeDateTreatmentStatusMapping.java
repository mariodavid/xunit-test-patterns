package com.haulmont.sample.petclinic.test_patterns.conditional_test_logic.production_logic_in_test.solution.test_your_tests;

import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.entity.visit.VisitTreatmentStatus;
import com.haulmont.sample.petclinic.test_patterns.conditional_test_logic.production_logic_in_test.SomethingStrangeHappenedInTheUniverseException;
import java.time.LocalDate;

public class RelativeDateTreatmentStatusMapping {


    private LocalDate today;

    public RelativeDateTreatmentStatusMapping(LocalDate today) {
        this.today = today;
    }

    public VisitTreatmentStatus mapToTreatmentStatus(Visit visit) {
        if (visit.getVisitStart().toLocalDate().isAfter(today)) {
            return VisitTreatmentStatus.UPCOMING;
        }
        else if (visit.getVisitStart().toLocalDate().equals(today)) {
            return VisitTreatmentStatus.IN_PROGRESS;
        }
        else if (visit.getVisitStart().toLocalDate().isBefore(today)) {
            return VisitTreatmentStatus.DONE;
        }
        else {
            throw new SomethingStrangeHappenedInTheUniverseException();
        }
    }

}
