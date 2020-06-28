package com.haulmont.sample.petclinic.test_patterns.code_smells.conditional_test_logic.production_logic_in_test.solution.unfold_complexity;


import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.config.PetclinicTestdataConfig;
import com.haulmont.sample.petclinic.core.RandomVisitDateTime;
import com.haulmont.sample.petclinic.core.VisitTestDataCreation;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.entity.visit.VisitTreatmentStatus;
import com.haulmont.sample.petclinic.test_patterns.code_smells.conditional_test_logic.production_logic_in_test.PetclinicData;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoProductionLogicInTestByUnfoldingTest {

    /**
     * NOW is freezed to some wednesday in order to not accidentally hit the weekend limitation
     * of the visit test data generation
     */
    private static final LocalDate TODAY = LocalDate.now().with(DayOfWeek.WEDNESDAY);
    private static final LocalDate TOMORROW = TODAY.plusDays(1);
    private static final LocalDate YESTERDAY = TODAY.minusDays(1);
    private static final ZonedDateTime NOW = TODAY.atStartOfDay(ZoneId.of("Europe/Berlin")).plusHours(8);

    VisitTestDataCreation visitTestDataCreation;

    @Mock
    PetclinicTestdataConfig petclinicTestdataConfig;
    @Mock
    TimeSource timeSource;
    @Mock
    DataManager dataManager;

    PetclinicData data;


    @BeforeEach
    void setup() {

        lenient().when(dataManager.create(Visit.class))
                .then(invocation -> new Visit());
        lenient().when(timeSource.now())
                .thenReturn(NOW);

        visitTestDataCreation = new VisitTestDataCreation(
            petclinicTestdataConfig,
            timeSource,
            dataManager,
            new RandomVisitDateTime()
        );

        data = new PetclinicData();

        possibleDescriptions("Fever, Disease");
    }

    @Test
    void when_createVisitForYesterday_thenTreatmentStatusIsDone() {

        // when:
        Visit visit = visitTestDataCreation.createVisit(
            YESTERDAY,
            singletonList(data.pet("Pikachu")),
            singletonList(data.nurse("Joy"))
        );

        // then:
        assertThat(visit.getTreatmentStatus())
                .isEqualTo(VisitTreatmentStatus.DONE);
    }


    @Test
    void when_createVisitForToday_thenTreatmentStatusIsUpComing() {

        // when:
        Visit visit = visitTestDataCreation.createVisit(
            TODAY,
            singletonList(data.pet("Pikachu")),
            singletonList(data.nurse("Joy"))
        );

        // then:
        assertThat(visit.getTreatmentStatus())
                .isEqualTo(VisitTreatmentStatus.IN_PROGRESS);
    }

    @Test
    void when_createVisitForTomorrow_thenTreatmentStatusIsUpComing() {

        // when:
        Visit visit = visitTestDataCreation.createVisit(
            TOMORROW,
            singletonList(data.pet("Pikachu")),
            singletonList(data.nurse("Joy"))
        );

        // then:
        assertThat(visit.getTreatmentStatus())
                .isEqualTo(VisitTreatmentStatus.UPCOMING);
    }

    private void possibleDescriptions(String descriptions) {
        lenient().when(petclinicTestdataConfig.getTestdataVisitDescriptionOptions())
                .thenReturn(descriptions);
    }

}