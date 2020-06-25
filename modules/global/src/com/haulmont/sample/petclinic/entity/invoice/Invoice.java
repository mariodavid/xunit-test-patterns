package com.haulmont.sample.petclinic.entity.invoice;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@NamePattern("%s|invoiceNumber")
@Table(name = "PETCLINIC_INVOICE")
@Entity(name = "petclinic_Invoice")
public class Invoice extends StandardEntity {

    private static final long serialVersionUID = 3604078131448725806L;

    @Column(name = "INVOICE_NUMBER", nullable = false)
    private String invoiceNumber;


    @Column(name = "INVOICE_DATE", nullable = false)
    private LocalDate invoiceDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "VISIT_ID")
    private Visit visit;

    @OrderBy("position")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "invoice")
    private List<InvoiceItem> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCUMENT_ID")
    private FileDescriptor document;

    public FileDescriptor getDocument() {
        return document;
    }

    public void setDocument(FileDescriptor document) {
        this.document = document;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}