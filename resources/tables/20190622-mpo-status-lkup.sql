create table Prototype.MPO_STATUS_LKUP (
ID INTEGER NOT NULL,
CODE VARCHAR(10) NOT NULL,
DESCRIPTION VARCHAR(30) NOT NULL,
CREATED_USER VARCHAR(30)  NOT NULL,
CREATED_TS TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
UPDATED_USER VARCHAR(30) NOT NULL,
UPDATED_TS TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);