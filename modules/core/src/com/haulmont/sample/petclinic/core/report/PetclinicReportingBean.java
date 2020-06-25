package com.haulmont.sample.petclinic.core.report;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.reports.ReportingBean;
import com.haulmont.reports.entity.ReportRunParams;
import com.haulmont.yarg.reporting.ReportOutputDocument;

public class PetclinicReportingBean extends ReportingBean {

    @Override
    protected FileDescriptor createAndSaveReportDocument(ReportRunParams reportRunParams) {
        ReportOutputDocument reportOutputDocument = createReportDocument(reportRunParams);
        byte[] reportData = reportOutputDocument.getContent();
        String documentName = reportOutputDocument.getDocumentName();
        String ext = reportRunParams.getReportTemplate().getReportOutputType().toString().toLowerCase();

        return saveReport(reportData, documentName.replaceAll(".docx", ""), ext);
    }
}