CREATE TABLE Users (
    UserId     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    email      VARCHAR NOT NULL UNIQUE,
    Password   VARCHAR NOT NULL,
    FName      VARCHAR NOT NULL,
    LName      VARCHAR NOT NULL,
    Address    VARCHAR NOT NULL
);

CREATE TABLE Tables (
    TableId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    NSeat   INT     NOT NULL
);
-- Confirm with group on implementation of NSeat (see DatabaseOutline.txt)


CREATE TABLE Bookings (
    ItemId     INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,
    TableId    INTEGER  REFERENCES Tables (TableId) NOT NULL,
    DateTime   DATETIME NOT NULL,
    CustomerId VARCHAR  REFERENCES Users (UserId),
    No_Guests  INTEGER  REFERENCES Tables (NSeat)
);


CREATE TABLE MenuItem (
    ItemId   INTEGER        PRIMARY KEY AUTOINCREMENT NOT NULL,
    ItemType enum('drink', 'food', 'special') NOT NULL,
    ItemName VARCHAR        NOT NULL,
    Price    DECIMAL (2, 2) NOT NULL, // ( (2,2) indicates max price of £99.99
    Sold     INTEGER        NOT NULL
);


CREATE TABLE Bookings (
    ItemId     INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,
    TableId    INTEGER  REFERENCES Tables (TableId) NOT NULL,
    OrderType enum('eatin', 'takeaway', 'delivery') not null,
    DateTime   DATETIME NOT NULL,
    CustomerId VARCHAR  REFERENCES Users (UserId),
    No_Guests  INTEGER  REFERENCES Tables (NSeat)
);
