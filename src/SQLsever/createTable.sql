
    CREATE TABLE UserAccount(
    ID int PRIMARY KEY,
    username char(20) NOT NULL,
    password char(20) NOT NULL,
    role nvarchar(50) NOT NULL
)



CREATE TABLE Employee(
    employeeID int NOT NULL,
    foreign key (employeeID) references UserAccount(ID),
	PRIMARY KEY (employeeID),
    name nvarchar(50) NOT NULL,
    phone char(10) NOT NULL CHECK (phone LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    hourWage int NOT NULL,
)

CREATE TABLE Customer(
    customerID int NOT NULL,
	foreign key (customerID) references UserAccount(ID),
	PRIMARY KEY (customerID),
    name nvarchar(50) NOT NULL,
    phone char(10) NOT NULL CHECK (phone LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    point int NOT NULL CHECK(point>=0),
)

CREATE TABLE TableCaffe (
    TableID int PRIMARY KEY,
    tableName nvarchar(50) NOT NULL,
    [status] nvarchar(50) NOT NULL
)


CREATE TABLE Orders(
    orderID int PRIMARY KEY,
    tableID int NOT NULL,
    employeeID int NOT NULL,
    customerID int NOT NULL,
    orderTime datetime NOT NULL,
    totalPrice decimal(10,2) NOT NULL,
    [status] nvarchar(50) NOT NULL,
    foreign key (tableID) references TableCaffe(TableID),
    foreign key (employeeID) references Employee(employeeID),
    foreign key (customerID) references Customer(customerID)
)

create table [Product](
    productID int PRIMARY KEY,
    name nvarchar(50) NOT NULL,
    price decimal(10,2) NOT NULL,
    size nvarchar(10) NOT NULL,
)

CREATE TABLE OrderDetail (
	orderDetailID int PRIMARY KEY,
    orderID int NOT NULL,
    productID int NOT NULL,
    quantity int NOT NULL CHECK (quantity > 0),
    price decimal(10,2) NOT NULL,
    foreign key (orderID) references Orders(orderID),
    foreign key (productID) references [Product](productID)
)

CREATE TABLE Payment(
	paymentID int PRIMARY KEY,
	orderID int NOT NULL,
	paymentMethod nvarchar(50) NOT NULL,
	amount decimal(10,2) NOT NULL,
	paymentTime datetime NOT NULL,
)

create table Ingredient(
    IngredientID int PRIMARY KEY,
    name nvarchar(50) NOT NULL,
    unit nvarchar(50) NOT NULL ,
    stockQuantity int NOT NULL CHECK(stockQuantity>=0)
)

create table ProductIngredient(
    productID int NOT NULL,
    ingredientID int NOT NULL,
    quantity decimal(10,2) NOT NULL,
    unit nvarchar(50) NOT NULL,
    foreign key (productID) references [Product](ProductID),
    foreign key (ingredientID) references Ingredient(ingredientID),
	PRIMARY KEY (productID,ingredientID)
)

create table Import(
    importID int PRIMARY KEY,
    ingredientID int NOT NULL,
    quantity int NOT NULL,
    unitPrice decimal(10,2) NOT NULL,
    importDate datetime NOT NULL,
    totalCost decimal(10,2) NOT NULL,
	foreign key (ingredientID) references Ingredient(ingredientID)
)

CREATE TABLE DeliveryOrders ( -- Giao hàng
	deliveryID INT PRIMARY KEY,
    orderID INT NOT NULL, 
    customerID INT NOT NULL,
    deliveryAddress NVARCHAR(255) NOT NULL,
    deliveryStatus NVARCHAR(50) NOT NULL DEFAULT 'Chưa giải quyết', -- Trạng thái
    deliveryTime DATETIME NULL,  -- Thời gian dự kiến
    FOREIGN KEY (orderID) REFERENCES Orders(orderID),
    FOREIGN KEY (customerID) REFERENCES Customer(customerID)
);
--Insert data
--Product
INSERT INTO Product (ProductID, name, price,size) VALUES
-- Coffee (categoryID = 1)
(1, N'Americano ', 49000,'M'),
(2, N'Americano ', 55000, 'L'),
(3, N'Espresso ', 49000, 'M'),
(4, N'Espresso ', 55000, 'L'),
(5, N'Caramel Macchiato ', 79000,'M'),
(6, N'Caramel Macchiato ', 85000, 'L'),
(7, N'Matcha Macchiato MD', 79000, 'M'),
(8, N'Matcha Macchiato LG', 85000, 'L'),
(9, N'Latte MD', 75000, 'M'),
(10, N'Latte LG', 79000, 'L'),
(11, N'Cappuchino ', 75000, 'M'),
(12, N'Cappuchino ', 79000, 'L'),
(13, N'Cold Brew', 69000, 'L'),
(14, N'Cold Brew Đào', 79000, 'L'),


(15, N'Matcha Latte ', 49000,'M'),
(16, N'Matcha Latte ', 55000, 'L'),
(17, N'Trà Thạch Vải ', 55000, 'M'),
(18, N'Trà Thạch Vải ', 65000, 'L'),
(19, N'Trà Thanh Đào ', 55000, 'M'),
(20, N'Trà Thanh Đào ', 65000, 'L'),
(21, N'Trà Sen Vàng ', 55000,'M'),
(22, N'Trà Sen Vàng ', 65000,'L'),
(23, N'Trà Xanh Đậu Đỏ ', 55000,'M'),
(24, N'Trà Xanh Đậu Đỏ ', 65000,'L'),


(25, N'Bánh Croissant', 29000,'M'),
(26, N'Bánh Mì Que Bò Sốt Phô Mai', 19000,'M'),
(27, N'Bánh Mì Que Gà Sốt Phô Mai', 19000,'M'),
(28, N'Bánh Mousse Đào', 35000,'M'),
(29, N'Bánh Mousse CaCao', 35000,'M'),
(30, N'Bánh Taramisu', 35000,'M'),
(31, N'Bánh Chuối', 35000,'M');

--Table
INSERT INTO TableCaffe(TableID, status, tableName) VALUES
(1, N'available', N'Bàn 1'),
(2, N'available', N'Bàn 2'),
(3, N'available', N'Bàn 3'),
(4, N'available', N'Bàn 4'),
(5, N'available', N'Bàn 5'),
(6, N'available', N'Bàn 6'),
(7, N'available', N'Bàn 7'),
(8, N'available', N'Bàn 8'),
(9, N'available', N'Bàn 9'),
(10, N'available', N'Bàn 10'),
(11, N'available', N'Bàn 11'),
(12, N'available', N'Bàn 12'),
(13, N'available', N'Bàn 13'),
(14, N'available', N'Bàn 14'),
(15, N'available', N'Bàn 15'),
(16, N'available', N'Bàn 16'),
(17, N'available', N'Bàn 17'),
(18, N'available', N'Bàn 18'),
(19, N'available', N'Bàn 19'),
(20, N'available', N'Bàn 20'),
(21, N'available', N'Bàn 21'),
(22, N'available', N'Bàn 22'),
(23, N'available', N'Bàn 23'),
(24, N'available', N'Bàn 24'),
(25, N'available', N'Bàn 25'),
(26, N'available', N'Bàn 26'),
(27, N'available', N'Bàn 27'),
(28, N'available', N'Bàn 28'),
(29, N'available', N'Bàn 29'),
(30, N'available', N'Bàn 30');
--UserSystem
INSERT INTO UserAccount (ID, username, password, role) VALUES
(1, 'quy', 'password', N'Quản lí'),
(2, 'minh', 'password', N'Quản lí'),
(3, 'vuong', 'password', N'Quản lí'),
(4, 'duypham', 'pass1234', N'thu ngân'),
(5, 'maitrinh', 'pass1234', N'thu ngân'),
(6,  'sonle', 'pass1234', N'thu ngân'),
(7, 'trangngo', 'pass1234', N'pha chế'),
(8, 'khangtong', 'pass1234', N'pha chế'),
(9, 'habui', 'pass1234', N'pha chế'),
(10,  'lanvu', 'pass1234', N'phục vụ'),
(11, 'toanhuynh', 'pass1234', N'phục vụ'),
(12,  'kimta', 'pass1234', N'phục vụ'),
(13, 'hangcao', 'pass1234', N'pha chế'),
(15, 'tailuong', 'pass1234', N'phục vụ'),
(16,   'huyendo', 'pass1234', N'thu ngân'),
(17,   'tuankieu', 'pass1234', N'pha chế'),
(18,  'nhungla', 'pass1234', N'phục vụ');
--Ingredient
INSERT INTO Ingredient (IngredientID, name, unit, stockQuantity) VALUES
(1, N'Cà phê hạt rang', N'gram', 750),
(2, N'Sữa tươi', N'ml', 900),
(3, N'Sữa đặc', N'ml', 650),
(4, N'Syrup Caramel', N'ml', 400),
(5, N'Syrup Đào', N'ml', 300),
(6, N'Syrup Vải', N'ml', 500),
(7, N'Bột Matcha', N'gram', 450),
(8, N'Trà Xanh', N'gram', 700),
(9, N'Trà Thanh Đào', N'gram', 350),
(10, N'Trà Sen Vàng', N'gram', 300),
(11, N'Trà Xanh Đậu Đỏ', N'gram', 300),
(12, N'Đá viên', N'gram', 1000),
(13, N'Bột Cacao', N'gram', 600),
(14, N'Bột mì', N'gram', 800),
(15, N'Bơ', N'gram', 500),
(16, N'Trứng', N'gram', 400),
(17, N'Phô mai', N'gram', 500),
(20, N'Đường', N'gram', 900),
(21, N'Sô cô la', N'gram', 500),
(22, N'Chuối', N'Trái',50);
