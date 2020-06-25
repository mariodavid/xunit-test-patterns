package com.haulmont.sample.petclinic.web.screens.invoice;

import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.invoice.Invoice;

@UiController("petclinic_Invoice.edit")
@UiDescriptor("invoice-edit.xml")
@EditedEntityContainer("invoiceDc")
@LoadDataBeforeShow
public class InvoiceEdit extends StandardEditor<Invoice> {
}