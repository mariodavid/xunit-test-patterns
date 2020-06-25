create table PETCLINIC_INVOICE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    INVOICE_NUMBER varchar(255) not null,
    INVOICE_DATE date not null,
    VISIT_ID varchar(36) not null,
    --
    primary key (ID)
);