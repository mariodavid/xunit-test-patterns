package com.haulmont.sample.petclinic.web.pet.pet;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.InputDialogFacet.CloseEvent;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Slider;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.owner.Owner;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.pet.PetType;

import com.haulmont.sample.petclinic.entity.visit.Visit;
import com.haulmont.sample.petclinic.entity.visit.VisitType;
import com.haulmont.sample.petclinic.service.VisitService;
import javax.inject.Inject;

@UiController("petclinic_Pet.browse")
@UiDescriptor("pet-browse.xml")
@LookupComponent("petsTable")
@LoadDataBeforeShow
@Route("pets")
public class PetBrowse extends StandardLookup<Pet> {

    @Inject
    protected Slider birthDateFilterField;
    @Inject
    protected TextField<String> idFilterField;
    @Inject
    protected LookupField<Owner> ownerFilterField;
    @Inject
    protected LookupField<PetType> typeFilterField;
    @Inject
    protected GroupTable<Pet> petsTable;
    @Inject
    protected Notifications notifications;
    @Inject
    protected VisitService visitCreationService;
    @Inject
    protected ScreenBuilders screenBuilders;
    @Inject
    protected MessageBundle messageBundle;

    @Subscribe("petsTable.clearFilter")
    protected void onPetsTableClearFilter(Action.ActionPerformedEvent event) {
        typeFilterField.setValue(null);
        ownerFilterField.setValue(null);
        idFilterField.setValue(null);
        birthDateFilterField.setValue(null);
    }


    @Subscribe("petsTable.createVisit")
    protected void onPetsTableCreateVisit(ActionPerformedEvent event) {

        final Visit createdVisit = visitCreationService
            .createVisitForToday(
                petsTable.getSingleSelected().getIdentificationNumber()
            );

        notifications.create(NotificationType.TRAY)
            .withCaption(messageBundle.getMessage("visitCreated"))
            .show();

        screenBuilders.editor(Visit.class, this)
            .editEntity(createdVisit)
            .withOpenMode(OpenMode.DIALOG)
            .show();
    }
}