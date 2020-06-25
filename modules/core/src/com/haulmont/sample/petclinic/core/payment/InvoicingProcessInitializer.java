package com.haulmont.sample.petclinic.core.payment;


import com.google.common.collect.Lists;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.app.UniqueNumbersAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.security.app.Authenticated;
import com.haulmont.reports.ReportingApi;
import com.haulmont.reports.entity.Report;
import com.haulmont.sample.petclinic.core.visit.VisitCompletedEvent;
import com.haulmont.sample.petclinic.entity.invoice.Invoice;
import com.haulmont.sample.petclinic.entity.invoice.InvoiceItem;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("petclinic_invoicingProcessInitializer")
public class InvoicingProcessInitializer {


    private static final Logger log = LoggerFactory.getLogger(InvoicingProcessInitializer.class);
    @Inject
    protected TimeSource timeSource;

    @Inject
    private DataManager dataManager;

    @Inject
    private UniqueNumbersAPI uniqueNumbersAPI;

    @Inject
    protected ReportingApi reportingApi;

    @Async
    @EventListener
    @Transactional
    @Authenticated
    public void onApplicationEvent(VisitCompletedEvent event) throws InterruptedException {
        log.info("Invoice process initialized: {}", event.getVisit());

        letsTakeABreak();

        CommitContext commitContext = new CommitContext();
        Invoice invoice = createInvoiceFor(event.getVisit(), commitContext);

        dataManager.commit(commitContext);

        generateInvoiceDocument(invoice);

    }

    private void generateInvoiceDocument(Invoice invoice) throws InterruptedException {

        letsTakeABreak();

        final Invoice persistedInvoice = dataManager.reload(invoice, "invoice-with-details-view");

        final FileDescriptor invoiceDocument = createReportFileDescriptor(persistedInvoice);
        persistedInvoice.setDocument(invoiceDocument);

        dataManager.commit(persistedInvoice);
    }

    private void letsTakeABreak() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
    }

    private Invoice createInvoiceFor(Visit visit, CommitContext commitContext) {
        Invoice invoice = dataManager.create(Invoice.class);

        invoice.setVisit(visit);
        invoice.setInvoiceDate(visit.getVisitStart().toLocalDate());
        invoice.setInvoiceNumber(createInvoiceNumber());

        List<InvoiceItem> invoiceItems = createInvoiceItemsFor(invoice);
        invoice.setItems(invoiceItems);

        invoiceItems.forEach(commitContext::addInstanceToCommit);
        commitContext.addInstanceToCommit(invoice);

        return invoice;
    }

    private List<InvoiceItem> createInvoiceItemsFor(Invoice invoice) {
        InvoiceItem invoiceItem = dataManager.create(InvoiceItem.class);

        invoiceItem.setInvoice(invoice);
        invoiceItem.setPosition(1);
        invoiceItem.setText("Visit flat fee");
        invoiceItem.setPrice(new BigDecimal("150.0"));

        return Lists.newArrayList(invoiceItem);
    }

    private String createInvoiceNumber() {
        final int year = timeSource.now().toLocalDate().getYear();
        return year + String.format("%06d", uniqueNumbersAPI.getNextNumber("vists_" + year));
    }


    private FileDescriptor createReportFileDescriptor(Invoice invoice) {
        return reportingApi.createAndSaveReport(
            loadReportByCode("visit-invoice"),
            ParamsMap.of("entity", invoice),
            getFilename(invoice)
        );
    }

    private Report loadReportByCode(String code) {
        return dataManager.load(Report.class)
            .query("select e from report$Report e where e.code = :reportCode")
            .parameter("reportCode", code).one();
    }

    private static String getFilename(Invoice invoice) {
        return "invoice_" + invoice.getInvoiceNumber();
    }


}
