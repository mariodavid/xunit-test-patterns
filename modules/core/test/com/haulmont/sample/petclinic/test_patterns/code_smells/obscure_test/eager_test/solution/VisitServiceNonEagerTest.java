package com.haulmont.sample.petclinic.test_patterns.code_smells.obscure_test.eager_test.solution;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.PetclinicTestContainer;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.service.VisitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class VisitServiceNonEagerTest {

    @RegisterExtension
    public static PetclinicTestContainer testContainer = PetclinicTestContainer.Common.INSTANCE;

    private static TimeSource timeSource;
    private static VisitService visitService;

    private static PetclinicVisitDb db;

    private Visit visit;
    private Pet pikachu;

    @BeforeAll
    public static void setupEnvironment() {
        visitService = AppBeans.get(VisitService.class);

        timeSource = AppBeans.get(TimeSource.class);

        db = new PetclinicVisitDb(
            AppBeans.get(DataManager.class),
            testContainer
        );
    }

    @BeforeEach
    public void loadPikachu() {
        pikachu = db.petWithName("Pikachu", "pet-with-owner-and-type");
    }

    /**
     * Solution:
     *
     * - splitted test cases into three dedicated test methods
     * - describe each case as what it deals with in order to help the "test as documentation" goal
     */
    @Test
    public void createVisitForToday_createsANewVisit_forTheCorrectPet() {

        // given:
        assertThat(db.countVisitsFor(pikachu))
            .isEqualTo(0);

        // when:
        visit = visitService.createVisitForToday(pikachu.getIdentificationNumber());

        // then:
        assertThat(db.countVisitsFor(pikachu))
            .isEqualTo(1);
    }

    @Test
    public void createVisitForToday_createsANewVisit_withTheCorrectVisitInformation() {

        // given:
        assertThat(db.countVisitsFor(pikachu))
            .isEqualTo(0);

        // when:
        visit = visitService.createVisitForToday(pikachu.getIdentificationNumber());

        // then:
        assertThat(visit.getPet())
            .isEqualTo(pikachu);

        // and:
        assertThat(visit.getVisitStart().toLocalDate())
            .isEqualTo(timeSource.now().toLocalDate());

    }

    @Test
    public void createVisitForToday_createsNoVisit_forAnIncorrectIdentificationNumber() {

        // given:
        String incorrectIdentificationNumber = "IncorrectIdentificationNumber";

        assertThat(db.petWithIdentificationNumber(incorrectIdentificationNumber))
            .isNotPresent();

        // and:
        Long amountOfVisitsBefore = db.countVisits();

        // when:
        visit = visitService.createVisitForToday(incorrectIdentificationNumber);

        // then:
        assertThat(visit)
            .isNull();

        // and:
        assertThat(db.countVisits())
            .isEqualTo(amountOfVisitsBefore);
    }

    @AfterEach
    public void cleanupVisit() {
        db.remove(visit);
    }

}