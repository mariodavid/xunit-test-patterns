package com.haulmont.sample.petclinic.test_patterns.code_smells.conditional_test_logic.production_logic_in_test.solution.test_your_tests;


import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.config.PetclinicTestdataConfig;
import com.haulmont.sample.petclinic.core.PetclinicData;
import com.haulmont.sample.petclinic.core.RandomVisitDateTime;
import com.haulmont.sample.petclinic.core.VisitTestDataCreation;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.test_patterns.code_smells.conditional_test_logic.production_logic_in_test.PetclinicMocking;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoProductionLogicInTestViaTestedUtilityMethodTest {

    /**
     * NOW is freezed to some wednesday in order to not accidentally hit the weekend limitation
     * of the visit test data generation
     */
    private static final LocalDate TODAY = LocalDate.now().with(DayOfWeek.WEDNESDAY);
    private static final ZonedDateTime NOW = TODAY.atStartOfDay(ZoneId.of("Europe/Berlin")).plusHours(8);

    VisitTestDataCreation visitTestDataCreation;

    @Mock
    PetclinicTestdataConfig petclinicTestdataConfig;
    @Mock
    TimeSource timeSource;
    @Mock
    DataManager dataManager;

    PetclinicData data;

    PetclinicMocking mocking;

    private final RelativeDateTreatmentStatusMapping treatmentStatusMapping =
        new RelativeDateTreatmentStatusMapping(TODAY);


    @BeforeEach
    void setup() {

        when(dataManager.create(Visit.class))
            .then(invocation -> new Visit());

        visitTestDataCreation = new VisitTestDataCreation(
            petclinicTestdataConfig,
            timeSource,
            dataManager,
            new RandomVisitDateTime()
        );

        when(timeSource.now())
            .thenReturn(NOW);

        data = new PetclinicData();

        mocking = new PetclinicMocking(
            dataManager,
            petclinicTestdataConfig
        );

        mocking.possibleDescriptions("Fever, Disease");

        mocking.possiblePets(
            data.pet("Pikachu"),
            data.pet("Garchomp")
        );

        mocking.possibleNurses(asList(data.nurse("Joy")));

    }

    /**
     * Problems:
     *
     * - what does "thenTreatmentStatusShouldBeDifferent" even mean in the concrete???
     * -
     */
    @Test
    void when_generateVisits_thenTreatmentStatusShouldBeDifferent() {

        // given:
        mocking.daysInPastToGenerateFor(5);
        mocking.daysInFutureToGenerateFor(6);
        mocking.visitAmountPerDay(10);

        // when:
        List<Visit> visits = visitTestDataCreation.createVisits();

        // then: depending on the visit start, the treatment status should be different
        visits
            .forEach(visit ->
                assertThat(visit.getTreatmentStatus())
                .isEqualTo(treatmentStatusMapping.mapToTreatmentStatus(visit))
            );

    }

}