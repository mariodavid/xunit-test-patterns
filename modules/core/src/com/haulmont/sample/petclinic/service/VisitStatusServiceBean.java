package com.haulmont.sample.petclinic.service;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.sample.petclinic.core.visit.VisitCompletedEvent;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.entity.visit.VisitTreatmentStatus;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(VisitStatusService.NAME)
public class VisitStatusServiceBean implements VisitStatusService {

    private static final Logger log = LoggerFactory.getLogger(VisitStatusServiceBean.class);

    @Inject
    private Events events;

    @Inject
    private DataManager dataManager;


    @Override
    public boolean updateVisitStatus(Visit visit, VisitTreatmentStatus status) {

        final Visit updatedVisit = updateStatus(visit, status);

        if (status.equals(VisitTreatmentStatus.DONE)) {
            notifyAboutVisitCompletion(updatedVisit);
        }

        return true;
    }



    private void notifyAboutVisitCompletion(Visit visit) {
        events.publish(new VisitCompletedEvent(this, visit));
    }

    private Visit updateStatus(Visit visit, VisitTreatmentStatus status) {
        visit.setTreatmentStatus(status);
        final Visit updatedVisit = dataManager.commit(visit);
        log.info("Visit {} status updated to {}", visit, status);

        return updatedVisit;
    }
}