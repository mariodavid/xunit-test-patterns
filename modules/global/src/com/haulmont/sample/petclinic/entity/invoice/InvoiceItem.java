package com.haulmont.sample.petclinic.entity.invoice;

import com.haulmont.cuba.core.entity.StandardEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "PETCLINIC_INVOICE_ITEM")
@Entity(name = "petclinic_InvoiceItem")
public class InvoiceItem extends StandardEntity {

    private static final long serialVersionUID = -8941594654782028988L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INVOICE_ID")
    private Invoice invoice;

    @Column(name = "POSITION_", nullable = false)
    private Integer position;

    @Column(name = "TEXT", nullable = false)
    private String text;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}