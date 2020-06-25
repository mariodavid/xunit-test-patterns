package com.haulmont.sample.petclinic.test_patterns.hard_to_test_code.async_code.solution;

import static com.haulmont.sample.petclinic.test_patterns.hard_to_test_code.async_code.problem.PollingUtils.untilPresent;
import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.sample.petclinic.PetclinicTestContainer;
import com.haulmont.sample.petclinic.autoimport.importprocessor.ReportsAutoImportProcessor;
import com.haulmont.sample.petclinic.core.payment.InvoicingProcessInitialization;
import com.haulmont.sample.petclinic.core.visit.VisitCompletedEvent;
import com.haulmont.sample.petclinic.entity.invoice.Invoice;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.service.VisitService;
import com.haulmont.sample.petclinic.service.VisitStatusService;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;


public class InvoicingProcessInitializationTest {

    @RegisterExtension
    public static PetclinicTestContainer testContainer = PetclinicTestContainer.Common.INSTANCE;

    private static VisitStatusService visitStatusService;
    private static InvoicingProcessInitialization invoicingProcessInitialization;
    private static VisitService visitService;

    private static PetclinicVisitDb db;

    private Visit visit;
    private Pet pikachu;
    private Optional<Invoice> invoice;

    @BeforeAll
    public static void setupEnvironment() {
        visitService = AppBeans.get(VisitService.class);
        visitStatusService = AppBeans.get(VisitStatusService.class);
        invoicingProcessInitialization = AppBeans.get(InvoicingProcessInitialization.class);


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
        visit = db.createCompletedVisitForPet(pikachu);

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
    public void when_updateVisitStatus_then_invoiceWillBeGenerated() throws Exception {

        importInvoiceReport();

        // when:
        invoicingProcessInitialization.createInvoice(
            visit
        );

        // then:
        invoice = db.invoiceForVisit(visit);

        assertThat(invoice)
            .isPresent();
    }

    private void importInvoiceReport() throws Exception {
        final ReportsAutoImportProcessor reportsAutoImportProcessor = AppBeans
            .get(ReportsAutoImportProcessor.class);

        reportsAutoImportProcessor.processFile("com/haulmont/sample/petclinic/autoimport/invoice_report.zip");
    }

    @AfterEach
    public void cleanup() {
        invoice.ifPresent(this::removeInvoice);
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