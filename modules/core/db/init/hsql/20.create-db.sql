-- begin PETCLINIC_PET
alter table PETCLINIC_PET add constraint FK_PETCLINIC_PET_TYPE foreign key (TYPE_ID) references PETCLINIC_PET_TYPE(ID)^
alter table PETCLINIC_PET add constraint FK_PETCLINIC_PET_OWNER foreign key (OWNER_ID) references PETCLINIC_OWNER(ID)^
create unique index IDX_PETCLINIC_PET_ID_UNQ on PETCLINIC_PET (IDENTIFICATION_NUMBER, DELETE_TS) ^
create index IDX_PETCLINIC_PET_TYPE on PETCLINIC_PET (TYPE_ID)^
create index IDX_PETCLINIC_PET_OWNER on PETCLINIC_PET (OWNER_ID)^
-- end PETCLINIC_PET
-- begin PETCLINIC_VISIT
alter table PETCLINIC_VISIT add constraint FK_PETCLINIC_VISIT_ASSIGNED_NURSE foreign key (ASSIGNED_NURSE_ID) references SEC_USER(ID)^
alter table PETCLINIC_VISIT add constraint FK_PETCLINIC_VISIT_PET foreign key (PET_ID) references PETCLINIC_PET(ID)^
create index IDX_PETCLINIC_VISIT_ASSIGNED_NURSE on PETCLINIC_VISIT (ASSIGNED_NURSE_ID)^
create index IDX_PETCLINIC_VISIT_PET on PETCLINIC_VISIT (PET_ID)^
-- end PETCLINIC_VISIT
-- begin PETCLINIC_VETERINARIAN_SPECIALTY_LINK
alter table PETCLINIC_VETERINARIAN_SPECIALTY_LINK add constraint FK_VETSPE_VETERINARIAN foreign key (VET_ID) references PETCLINIC_VETERINARIAN(ID)^
alter table PETCLINIC_VETERINARIAN_SPECIALTY_LINK add constraint FK_VETSPE_SPECIALTY foreign key (SPECIALTY_ID) references PETCLINIC_SPECIALTY(ID)^
-- end PETCLINIC_VETERINARIAN_SPECIALTY_LINK
-- begin PETCLINIC_INVOICE
alter table PETCLINIC_INVOICE add constraint FK_PETCLINIC_INVOICE_VISIT foreign key (VISIT_ID) references PETCLINIC_VISIT(ID)^
alter table PETCLINIC_INVOICE add constraint FK_PETCLINIC_INVOICE_DOCUMENT foreign key (DOCUMENT_ID) references SYS_FILE(ID)^
create index IDX_PETCLINIC_INVOICE_VISIT on PETCLINIC_INVOICE (VISIT_ID)^
create index IDX_PETCLINIC_INVOICE_DOCUMENT on PETCLINIC_INVOICE (DOCUMENT_ID)^
-- end PETCLINIC_INVOICE
-- begin PETCLINIC_INVOICE_ITEM
alter table PETCLINIC_INVOICE_ITEM add constraint FK_PETCLINIC_INVOICE_ITEM_INVOICE foreign key (INVOICE_ID) references PETCLINIC_INVOICE(ID)^
create index IDX_PETCLINIC_INVOICE_ITEM_INVOICE on PETCLINIC_INVOICE_ITEM (INVOICE_ID)^
-- end PETCLINIC_INVOICE_ITEM
