package com.haulmont.sample.petclinic.web.screens.invoice;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.HBoxLayout;
import com.haulmont.cuba.gui.components.LinkButton;
import com.haulmont.cuba.gui.components.ProgressBar;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Timer.TimerActionEvent;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionContainer.CollectionChangeEvent;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.invoice.Invoice;
import javax.inject.Inject;

@UiController("petclinic_Invoice.browse")
@UiDescriptor("invoice-browse.xml")
@LookupComponent("invoicesTable")
@LoadDataBeforeShow
public class InvoiceBrowse extends StandardLookup<Invoice> {

    @Inject
    protected ExportDisplay exportDisplay;
    @Inject
    protected GroupTable<Invoice> invoicesTable;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected CollectionContainer<Invoice> invoicesDc;
    @Inject
    protected Timer autoRefreshTimer;
    @Inject
    protected Notifications notifications;

    @Subscribe("invoicesTable.download")
    protected void onInvoicesTableDownload(ActionPerformedEvent event) {
        downloadDocument(invoicesTable.getSingleSelected());
    }

    private void downloadDocument(Invoice invoice) {
        exportDisplay.show(invoice.getDocument());
    }

    @Install(to = "invoicesTable.document", subject = "columnGenerator")
    protected Component invoicesTableDocumentColumnGenerator(Invoice invoice) {

        if (invoice.getDocument() == null) {
            return renderProgressBar();
        }

        final LinkButton documentLink = uiComponents.create(LinkButton.class);
        documentLink.setIconFromSet(CubaIcon.FILE_WORD_O);
        documentLink.setCaption(invoice.getDocument().getName());

        documentLink.setAction(
            new BaseAction("download")
                .withHandler(event -> downloadDocument(invoice))
        );

        return documentLink;

    }

    private Component renderProgressBar() {
        HBoxLayout layout = uiComponents.create(HBoxLayout.class);
        layout.setSpacing(true);
        ProgressBar progressBar = uiComponents.create(ProgressBar.class);
        progressBar.setWidth("200px");

        progressBar.setIndeterminate(true);
        return progressBar;

    }

    @Subscribe(id = "invoicesDc", target = Target.DATA_CONTAINER)
    protected void onInvoicesDcCollectionChange(CollectionChangeEvent<Invoice> event) {
        if (notAllDocumentsAreGenerated()) {
            startAutoRefresh();
        }
    }

    @Subscribe("autoRefreshTimer")
    protected void onAutoRefreshTimerTimerAction(TimerActionEvent event) {
        refreshData();

        if (allDocumentsAreGenerated()) {
            stopAutoRefresh();
        }
    }

    private void stopAutoRefresh() {
        autoRefreshTimer.stop();
    }

    private void startAutoRefresh() {
        autoRefreshTimer.start();
    }

    private void refreshData() {
        getScreenData().loadAll();
    }

    private boolean allDocumentsAreGenerated() {
        return invoicesDc.getItems()
            .stream()
            .allMatch(invoice -> invoice.getDocument() != null);
    }

    private boolean notAllDocumentsAreGenerated() {
        return !allDocumentsAreGenerated();
    }

}