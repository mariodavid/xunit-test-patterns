package com.haulmont.sample.petclinic.test_patterns.hard_to_test_code.async_code.solution;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.sample.petclinic.PetclinicTestContainer;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.entity.visit.VisitTreatmentStatus;
import com.haulmont.sample.petclinic.service.VisitService;
import com.haulmont.sample.petclinic.service.VisitStatusService;
import com.haulmont.sample.petclinic.test_patterns.hard_to_test_code.async_code.problem.PetclinicVisitDb;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;


public class VisitStatusServiceSmokeTest {

    @RegisterExtension
    public static PetclinicTestContainer testContainer = PetclinicTestContainer.Common.INSTANCE;

    private static VisitStatusService visitStatusService;
    private static VisitService visitService;

    private static PetclinicVisitDb db;

    private Visit visit;
    private Pet pikachu;

    @BeforeAll
    public static void setupEnvironment() {
        visitService = AppBeans.get(VisitService.class);
        visitStatusService = AppBeans.get(VisitStatusService.class);

        db = new PetclinicVisitDb(
            AppBeans.get(DataManager.class),
            testContainer
        );
    }

    @BeforeEach
    public void createVisitForPikachu() {

        pikachu = db.petWithName("Pikachu", "pet-with-owner-and-type");

        // given:
        assertThat(db.countVisitsFor(pikachu))
            .isEqualTo(0);

        // and:
        visit = visitService.createVisitForToday(
            pikachu.getIdentificationNumber()
        );

        assertThat(db.invoiceForVisit(visit))
            .isNotPresent();
    }

    @Test
    public void when_updateVisitStatus_then_weCanAssumeThatEventWasSendBecauseItReturnsTrue() {

        // when:
        boolean result = visitStatusService.updateVisitStatus(
            visit,
            VisitTreatmentStatus.DONE
        );

        // then:
        assertThat(result)
            .isTrue();

    }

    @AfterEach
    public void cleanup() {
        db.remove(visit);
    }

}