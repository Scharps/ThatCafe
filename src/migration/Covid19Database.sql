DROP DATABASE Cafe94;
CREATE DATABASE Cafe94;
USE Cafe94;

CREATE TABLE Address(
    AddressId Integer auto_increment, 
    FirstLine varchar(255) not null,
    City varchar(255) not null,
    PostCode varchar(10) not null,
    Primary key(AddressId)
);

Create Table Customers (
    CustomerId Integer auto_increment,  
    Username varchar(255) not null unique,  
    Password varchar(255) not null,  
    FName varchar(255) not null,  
    LName varchar(255) not null,
    AddressId Integer not  null,  
    Primary key (CustomerId),  
    Foreign Key (AddressId) references Address(AddressId)
);

create table Rota(
	RotaId Integer AUTO_INCREMENT,
	MonStart VARCHAR(5) default "None",
	MonFinish VARCHAR(5) default "None",
	TueStart VARCHAR(5) default "None",
	TueFinish VARCHAR(5) default "None",
	WedStart VARCHAR(5) default "None",
	WedFinish VARCHAR(5) default "None",
	ThuStart VARCHAR(5) default "None",
	ThuFinish VARCHAR(5) default "None",
	FriStart VARCHAR(5) default "None",
	FriFinish VARCHAR(5) default "None",
	SatStart VARCHAR(5) default "None",
	SatFinish VARCHAR(5) default "None",
	SunStart VARCHAR(5) default "None",
	SunFinish VARCHAR(5) default "None",
	Primary Key (RotaId)
);

Create Table Staff(
    StaffId Integer auto_increment,
    Password varchar(255) not null,
    FName varchar(255) not null,
    LName varchar(255) not null,
    StaffPos varchar(10) not null,
    RotaId INTEGER not null,
    FOREIGN KEY (RotaId) REFERENCES Rota(RotaId),
    Primary key(StaffId)
);

INSERT INTO Rota() VALUES();
INSERT INTO Staff(Password,FName,LName,StaffPos,RotaId) values ("1234","Mr.","Manager","Manager",1);

Create table CafeTables(
    TableId Integer auto_increment,
    Capacity Integer not null,
    Primary Key(TableId)
);

INSERT INTO CafeTables(Capacity) values (2),(2),(2),(2),(4),(4),(4),(4),(8),(8),(10);

create table MenuItems(
    ItemId integer auto_increment not null, 
    ItemName varchar(255) not null, 
    ItemType enum('Food', 'Drink') not null,
    Price Decimal(4,2) not null, 
    Sold Integer not null default 0,
    Special BOOLEAN DEFAULT FALSE,
    Primary key (ItemId)
);

INSERT INTO MenuItems(ItemName,ItemType,Price) VALUES ("Pizza", 'Food', 11.99), ('Beef Burger','Food', 8.99), ("Cola", 'Drink', 1.99), ("Beer", "Drink", 3.25);

Create Table Orders(
    OrderId Integer auto_increment not null,    
    OrderDate DATETIME not null,
    CustomerId Integer not null,  
    Cooked Boolean not null default 0,
    OrderTotal Decimal(5,2) not null default 0,
    OrderType enum('EatIn','Takeaway','Delivery') not null,
    Primary key (OrderId),  
    Foreign Key (CustomerId) references Customers(CustomerId)
);

Create table OrderedItems(
	OrderedItemID Integer auto_increment not null,
	OrderID Integer not null,
	ItemID Integer not null,
	Quantity INTEGER NOT NULL,
	Primary Key (OrderedItemID),
	Foreign Key (OrderID) references Orders(OrderID),
	Foreign Key (ItemID) references MenuItems(ItemID)
); 
    
 Create Table Bookings ( 
    BookingId Integer auto_increment not null UNIQUE,
    TableId Integer not null, 
    CustomerId Integer not null, 
    BookingDate DATE not null,
    BookingHour INT not null,
    GuestQuantity Integer not null,
    Approved boolean not null default 0,
    PRIMARY KEY(TableId, BookingDate),
    Foreign Key (TableId) references CafeTables(TableId), 
    Foreign Key (CustomerId) references Customers(CustomerId)
);
    
create table EatinOrders(
	OrderID Integer,
	TableID Integer not null,
	Primary Key (OrderId),
	Foreign Key (OrderId) references Orders(OrderId),
	Foreign Key (TableId) references CafeTables(TableId)
);

create table TakeawayOrders(
	OrderID Integer,
	PickUpTime DATETIME not null,
	Collected Boolean not null default 0,
	Primary Key (OrderId),
	Foreign Key (OrderId) references Orders(OrderId)
);

create table DeliveryOrders(
	OrderId Integer,
	Confirmed Boolean not null default 0,
	DeliveryTime DATETIME not null,
	DriverId Integer,
	Delivered Boolean not null default 0,
	Primary Key (OrderId),
	Foreign Key (OrderId) references Orders(OrderId),
	Foreign Key (DriverId) references Staff(StaffId)
);

CREATE TABLE HoursWorked (
	HoursWorkedId INTEGER AUTO_INCREMENT,
    StaffId INTEGER NOT NULL,
    DateWorked DATE NOT NULL,
    WorkedHours INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (HoursWorkedId),
    FOREIGN KEY (StaffId) REFERENCES Staff(StaffId)
);
    
