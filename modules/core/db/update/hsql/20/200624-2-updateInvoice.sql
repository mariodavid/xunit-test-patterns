-- alter table PETCLINIC_INVOICE add column DOCUMENT_ID varchar(36) ^
-- update PETCLINIC_INVOICE set DOCUMENT_ID = <default_value> ;
-- alter table PETCLINIC_INVOICE alter column DOCUMENT_ID set not null ;
alter table PETCLINIC_INVOICE add column DOCUMENT_ID varchar(36) not null ;
