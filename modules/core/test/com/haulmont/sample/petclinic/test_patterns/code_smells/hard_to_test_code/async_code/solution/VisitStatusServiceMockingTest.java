package com.haulmont.sample.petclinic.test_patterns.code_smells.hard_to_test_code.async_code.solution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.sample.petclinic.core.visit.VisitCompletedEvent;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.entity.visit.VisitTreatmentStatus;
import com.haulmont.sample.petclinic.service.VisitStatusService;
import com.haulmont.sample.petclinic.service.VisitStatusServiceBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VisitStatusServiceMockingTest {

    private VisitStatusService sut;

    @Spy
    Events events;

    @Mock
    DataManager dataManager;

    private Visit visit;

    @BeforeEach
    public void setup() {

        visit = new Visit();

        Mockito.when(dataManager.commit(any(Visit.class)))
            .thenReturn(visit);

        sut = new VisitStatusServiceBean(events, dataManager);

    }

    @Test
    public void when_updateVisitStatus_then_weJustVerifyTheSpy() {

        // when:
        sut.updateVisitStatus(
            visit,
            VisitTreatmentStatus.DONE
        );

        // then:
        assertThat(publishedEvent().getVisit())
            .isEqualTo(visit);

    }

    private VisitCompletedEvent publishedEvent() {
        ArgumentCaptor<VisitCompletedEvent> catchedEvent =
            ArgumentCaptor.forClass(VisitCompletedEvent.class);

        Mockito.verify(events)
            .publish(catchedEvent.capture());

        return catchedEvent.getValue();
    }
}