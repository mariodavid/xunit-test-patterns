package com.haulmont.sample.petclinic.service;

import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.entity.visit.VisitTreatmentStatus;

public interface VisitStatusService {

    String NAME = "petclinic_VisitStatusService";

    boolean updateVisitStatus(Visit visit, VisitTreatmentStatus status);
}