package com.haulmont.sample.petclinic.test_patterns.hard_to_test_code.async_code.problem;

import static com.haulmont.sample.petclinic.test_patterns.hard_to_test_code.async_code.problem.PollingUtils.untilPresent;
import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.sample.petclinic.PetclinicTestContainer;
import com.haulmont.sample.petclinic.entity.invoice.Invoice;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.entity.visit.VisitTreatmentStatus;
import com.haulmont.sample.petclinic.service.VisitService;
import com.haulmont.sample.petclinic.service.VisitStatusService;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;


public class VisitCompletedAsyncTest {

    @RegisterExtension
    public static PetclinicTestContainer testContainer = PetclinicTestContainer.Common.INSTANCE;

    private static VisitStatusService visitStatusService;
    private static VisitService visitService;

    private static PetclinicVisitDb db;

    private Visit visit;
    private Pet pikachu;
    private Invoice invoice;

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

    /**
     * Problems:
     *
     * - invoice is not generated synchronous
     * - algorithm and transport mechanism are combined in one test
     */
    @Test
    public void when_updateVisitStatus_then_invoiceWillBeGenerated() {

        // when:
        boolean result = visitStatusService.updateVisitStatus(
            visit,
            VisitTreatmentStatus.DONE
        );

        // then:
        assertThat(result)
            .isTrue();

        // and:
        invoice = untilPresent(() ->
            db.invoiceForVisit(visit)
        );

        assertThat(invoice)
            .isNotNull();
    }

    @Test
    public void when_updateVisitStatus_then_invoiceDateIsToday() {

        // when:
        boolean result = visitStatusService.updateVisitStatus(
            visit,
            VisitTreatmentStatus.DONE
        );

        // then:
        assertThat(result)
            .isTrue();

        // and:
        invoice = untilPresent(() ->
            db.invoiceForVisit(visit)
        );

        assertThat(invoice.getInvoiceDate())
            .isEqualTo(LocalDate.now());
    }


    @Test
    public void when_updateVisitStatus_then_invoiceContainsOneInvoiceItem() {

        // when:
        boolean result = visitStatusService.updateVisitStatus(
            visit,
            VisitTreatmentStatus.DONE
        );

        // then:
        assertThat(result)
            .isTrue();

        // and:
        invoice = untilPresent(() ->
            db.invoiceForVisit(visit)
        );

        assertThat(invoice.getItems())
            .hasSize(1);
    }

    @AfterEach
    public void cleanup() {
        removeInvoice(invoice);
        db.remove(visit);
    }

    private void removeInvoice(Invoice invoice) {
        invoice
            .getItems()
            .forEach(invoiceItem ->
                db.remove(invoiceItem)
            );

        db.remove(invoice);
    }
}