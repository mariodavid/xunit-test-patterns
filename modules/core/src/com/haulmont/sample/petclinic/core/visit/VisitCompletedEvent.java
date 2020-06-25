package com.haulmont.sample.petclinic.core.visit;

import com.haulmont.sample.petclinic.entity.visit.Visit;
import org.springframework.context.ApplicationEvent;

public class VisitCompletedEvent extends ApplicationEvent {

    private final Visit visit;

    public VisitCompletedEvent(Object source, Visit visit) {

        super(source);

        this.visit = visit;
    }

    public Visit getVisit() {
        return visit;
    }
}
