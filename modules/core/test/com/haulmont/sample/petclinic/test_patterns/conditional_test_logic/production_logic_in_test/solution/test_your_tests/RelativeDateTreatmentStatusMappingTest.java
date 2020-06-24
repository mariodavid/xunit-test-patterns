package com.haulmont.sample.petclinic.test_patterns.conditional_test_logic.production_logic_in_test.solution.test_your_tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.entity.visit.VisitTreatmentStatus;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RelativeDateTreatmentStatusMappingTest {


    private static final LocalDateTime TODAY = LocalDate.now().with(DayOfWeek.WEDNESDAY).atStartOfDay();
    private static final LocalDateTime TOMORROW = TODAY.plusDays(1);
    private static final LocalDateTime YESTERDAY = TODAY.minusDays(1);

    private RelativeDateTreatmentStatusMapping sut;


    @BeforeEach
    void setup() {
        sut = new RelativeDateTreatmentStatusMapping(LocalDate.now().with(DayOfWeek.WEDNESDAY));
    }

    @Test
    void given_todaysVisit_when_mapToTreatmentStatus_then_statusIsInProgress() {

        // given:
        Visit visit = visitFor(TODAY);

        // when:
        final VisitTreatmentStatus status = sut.mapToTreatmentStatus(visit);

        // then:
        assertThat(status)
            .isEqualTo(VisitTreatmentStatus.IN_PROGRESS);
    }

    @Test
    void given_yesterdaysVisit_when_mapToTreatmentStatus_then_statusIsDone() {

        // given:
        Visit visit = visitFor(YESTERDAY);

        // when:
        final VisitTreatmentStatus status = sut.mapToTreatmentStatus(visit);

        // then:
        assertThat(status)
            .isEqualTo(VisitTreatmentStatus.DONE);
    }


    @Test
    void given_tomorrowsVisit_when_mapToTreatmentStatus_then_statusIsUpcoming() {

        // given:
        Visit visit = visitFor(TOMORROW);

        // when:
        final VisitTreatmentStatus status = sut.mapToTreatmentStatus(visit);

        // then:
        assertThat(status)
            .isEqualTo(VisitTreatmentStatus.UPCOMING);
    }

    private Visit visitFor(LocalDateTime visitStart) {
        Visit visit = new Visit();
        visit.setVisitStart(visitStart);
        return visit;
    }
}