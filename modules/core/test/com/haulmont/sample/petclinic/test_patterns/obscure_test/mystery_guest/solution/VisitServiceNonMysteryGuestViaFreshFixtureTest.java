package com.haulmont.sample.petclinic.test_patterns.obscure_test.mystery_guest.solution;

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

public class VisitServiceNonMysteryGuestViaFreshFixtureTest {

    @RegisterExtension
    public static PetclinicTestContainer testContainer = PetclinicTestContainer.Common.INSTANCE;

    private static TimeSource timeSource;
    private static VisitService visitService;

    private static PetclinicVisitDb db;

    private Visit visit;
    private Pet pikachu;
    private Pet azumarill;

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
     * - introduce "fresh fixture"
     */
    @Test
    public void createVisitForToday_createsANewVisit_withFreshFixture() {

        // given:
        azumarill = db.createPet(
            "Azumarill",
            "184",
            "1999-07-03"
        );

        // when:
        visit = visitService.createVisitForToday("184");

        // then:
        assertThat(visit.getPet())
            .isEqualTo(azumarill);


        assertThat(db.countVisitsFor(azumarill))
            .isEqualTo(1);

        // and:
        assertThat(visit.getVisitStart().toLocalDate())
            .isEqualTo(timeSource.now().toLocalDate());

    }

    @AfterEach
    public void cleanupTestdata() {
        db.remove(visit);
        db.remove(azumarill);
    }

}