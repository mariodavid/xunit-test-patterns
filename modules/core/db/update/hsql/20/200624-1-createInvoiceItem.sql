create table PETCLINIC_INVOICE_ITEM (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    INVOICE_ID varchar(36) not null,
    POSITION_ integer not null,
    TEXT varchar(255) not null,
    PRICE decimal(19, 2) not null,
    --
    primary key (ID)
);