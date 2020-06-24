package com.haulmont.sample.petclinic.test_patterns.obscure_test.mystery_guest.problem;

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

public class VisitServiceMysteryGuestTest {

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
     * Problems:
     *
     * - where does "pikachu" come from? what attributes does it have?
     * - which pet is associated with the identification number "25"?
     * - how do we know how many visits are available for pikachu?
     * - what happens if another test also relies on pikachu and creates a visit for this test case?
     */
    @Test
    public void createVisitForToday_createsANewVisit_withMysteryGuest() {

        // when:
        visit = visitService.createVisitForToday("025");

        // then:
        assertThat(visit.getPet())
            .isEqualTo(pikachu);

        // and:
        assertThat(db.countVisitsFor(pikachu))
            .isEqualTo(1);

        // and:
        assertThat(visit.getVisitStart().toLocalDate())
            .isEqualTo(timeSource.now().toLocalDate());
    }

    @AfterEach
    public void cleanupVisit() {
        db.remove(visit);
    }

}