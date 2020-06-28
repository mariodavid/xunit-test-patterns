package com.haulmont.sample.petclinic.test_patterns.code_smells.hard_to_test_code.async_code.solution;

import static com.haulmont.sample.petclinic.test_patterns.code_smells.hard_to_test_code.async_code.problem.PollingUtils.untilPresent;
import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.sample.petclinic.PetclinicTestContainer;
import com.haulmont.sample.petclinic.autoimport.importprocessor.ReportsAutoImportProcessor;
import com.haulmont.sample.petclinic.core.payment.InvoicingProcessInitialization;
import com.haulmont.sample.petclinic.entity.invoice.Invoice;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;


public class InvoicingProcessInitializationTest {

    @RegisterExtension
    public static PetclinicTestContainer testContainer = PetclinicTestContainer.Common.INSTANCE;

    private static InvoicingProcessInitialization invoicingProcessInitialization;

    private static PetclinicVisitDb db;

    private Visit visit;
    private Pet pikachu;
    private Optional<Invoice> invoice;

    @BeforeAll
    public static void setupEnvironment() throws Exception {
        invoicingProcessInitialization = AppBeans.get(InvoicingProcessInitialization.class);

        db = new PetclinicVisitDb(
            AppBeans.get(DataManager.class),
            testContainer
        );

        importInvoiceReport();
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
     * Solution:
     *
     * - only the algorithm is tested in a synchronous fashion
     * - the transport mechanism is tested in a dedicated async test / via mocking
     */
    @Test
    public void when_createInvoice_then_invoiceWillBeGenerated() throws Exception {

        // when:
        invoicingProcessInitialization.createInvoice(
            visit
        );

        // then:
        invoice = db.invoiceForVisit(visit);

        assertThat(invoice)
            .isPresent();
    }

    @Test
    public void when_createInvoice_then_invoiceContainsOneItem() throws Exception {


        // when:
        invoicingProcessInitialization.createInvoice(
            visit
        );

        // then:
        invoice = db.invoiceForVisit(visit);

        assertThat(invoice)
            .isPresent();

        // and:
        assertThat(invoice.get().getItems())
            .hasSize(1);
    }

    private static void importInvoiceReport() throws Exception {
        final ReportsAutoImportProcessor reportsAutoImportProcessor = AppBeans
            .get(ReportsAutoImportProcessor.class);

        reportsAutoImportProcessor.processFile(
            "com/haulmont/sample/petclinic/autoimport/invoice_report.zip");
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