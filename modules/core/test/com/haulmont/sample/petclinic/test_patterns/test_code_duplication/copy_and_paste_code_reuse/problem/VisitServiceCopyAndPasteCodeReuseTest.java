package com.haulmont.sample.petclinic.test_patterns.test_code_duplication.copy_and_paste_code_reuse.problem;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.View;
import com.haulmont.sample.petclinic.PetclinicTestContainer;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.service.VisitService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class VisitServiceCopyAndPasteCodeReuseTest {

    @RegisterExtension
    public static PetclinicTestContainer testContainer = PetclinicTestContainer.Common.INSTANCE;

    private static TimeSource timeSource;
    private static VisitService visitService;

    private static DataManager dataManager;

    private Visit visit;
    private Pet pikachu;

    @BeforeAll
    public static void setupEnvironment() {
        visitService = AppBeans.get(VisitService.class);
        timeSource = AppBeans.get(TimeSource.class);
        dataManager = AppBeans.get(DataManager.class);
    }

    /**
     * Problems:
     *
     * - DB Interaction is duplicated across all test cases
     * - Test data setup is duplicated
     * - After copying the test cases, setup and / or the verification steps were adjusted
     */
    @Test
    public void createVisitForToday_createsANewVisit_forTheCorrectPet() {

        // given:
        pikachu = dataManager.load(Pet.class)
            .query("e.name = ?1", "Pikachu")
            .view("pet-with-owner-and-type")
            .one();

        // given:
        assertThat(dataManager.loadValue(
            "select count(e) from petclinic_Visit e where e.pet = :pet", Long.class)
            .parameter("pet", pikachu)
            .one())
            .isEqualTo(0);

        // when:
        visit = visitService.createVisitForToday(pikachu.getIdentificationNumber());

        // then:
        assertThat(dataManager.loadValue(
            "select count(e) from petclinic_Visit e where e.pet = :pet", Long.class)
            .parameter("pet", pikachu)
            .one())
            .isEqualTo(1);

        testContainer.deleteRecord(visit);
    }

    @Test
    public void createVisitForToday_createsANewVisit_withTheCorrectVisitInformation() {

        // given:
        pikachu = dataManager.load(Pet.class)
            .query("e.name = ?1", "Pikachu")
            .view("pet-with-owner-and-type")
            .one();

        // given:
        assertThat(dataManager.loadValue(
            "select count(e) from petclinic_Visit e where e.pet = :pet", Long.class)
            .parameter("pet", pikachu)
            .one())
            .isEqualTo(0);

        // when:
        visit = visitService.createVisitForToday(pikachu.getIdentificationNumber());

        // then:
        assertThat(visit.getPet())
            .isEqualTo(pikachu);

        // and:
        assertThat(visit.getVisitStart().toLocalDate())
            .isEqualTo(timeSource.now().toLocalDate());

        testContainer.deleteRecord(visit);

    }

    @Test
    public void createVisitForToday_createsNoVisit_forAnIncorrectIdentificationNumber() {

        // given:
        String incorrectIdentificationNumber = "IncorrectIdentificationNumber";

        assertThat(dataManager.load(Pet.class)
            .query("e.identificationNumber = ?1", incorrectIdentificationNumber)
            .view(View.LOCAL)
            .optional())
            .isNotPresent();

        // and:
        Long amountOfVisitsBefore = dataManager.loadValue(
            "select count(e) from petclinic_Visit e", Long.class)
            .one();

        // when:
        visit = visitService.createVisitForToday(incorrectIdentificationNumber);

        // then:
        assertThat(visit)
            .isNull();

        // and:
        assertThat(dataManager.loadValue(
            "select count(e) from petclinic_Visit e", Long.class)
            .one())
            .isEqualTo(amountOfVisitsBefore);

        testContainer.deleteRecord(visit);
    }

}