
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
	CCCD CHAR(12) NOT NULL CHECK (CCCD LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
	birthDate DATE NOT NULL,
	gender NVARCHAR(10) NOT NULL CHECK (gender IN (N'Nam', N'Nữ'))
)



CREATE TABLE Customer(
    customerID int NOT NULL,
	foreign key (customerID) references UserAccount(ID),
	PRIMARY KEY (customerID),
    name nvarchar(50) NOT NULL,
    phone char(10) NULL CHECK (phone LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
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
	image nvarchar(100) NOT NULL
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




--Insert data

-- userAccount
INSERT INTO UserAccount (ID, username, password, role) VALUES  -- Tên đăng nhập >6 ký tự, mật khẩu >8 ký tự gồm chữ chữ, số và kí tự đặc biệt
(100000, '','',N'Khách vãng lai'), -- Để phục vụ cho khách vãng lai (khách nào vãng lai thì lấy ID này, nhân viên có chức năng tạo khách vãng lai bằng một nút bấm cho thao tác nhanh hơn, không cần nhập ID khách)
(100001, 'quy123', 'quy1234@', N'Quản lí'),
(100002, 'minh123', 'minh123@', N'Quản lí'),
(100003, 'vuong123', 'vuong123@', N'Quản lí'),
(100004, 'duypham123', 'pass1234@', N'thu ngân'),
(100005, 'maitrinh123', 'trinh1234@@', N'thu ngân'),
(100006, 'trangngo12', 'pass12341234', N'pha chế'),
(100007, 'lanvu123', 'pass12344321', N'phục vụ')


--Employee
INSERT INTO Employee (employeeID, name, phone, hourWage, CCCD, birthDate, gender)
VALUES
(100001, N'Nguyễn Ngọc Quý', '0912345678', 0, '012345678901', '1990-05-12', N'Nam'),
(100002, N'Nguyễn Bình Minh', '0923456789', 0, '023456789012', '1992-07-25', N'Nam'),
(100003, N'Nguyễn Minh Vương', '0818214849', 0, '034567890123', '1988-03-18', N'Nam'),
(100004, N'Phạm Duy', '0945678901', 40000, '045678901234', '1995-11-02', N'Nam'),
(100005, N'Mai Trinh', '0956789012', 40000, '056789012345', '1997-09-14', N'Nữ'),
(100006, N'Ngô Trang', '0967890123', 45000, '067890123456', '2000-12-22', N'Nữ'),
(100007, N'Vũ Lan', '0978901234', 32000, '078901234567', '1999-06-30', N'Nữ');

-- Customer
INSERT INTO [dbo].[Customer]([customerID],[name],[phone],[point])
VALUES (100000, N'Khách vãng lai', '0000000000',0)


--Product
INSERT INTO Product (ProductID, name, price,size, image) VALUES
-- Coffee (categoryID = 1)
(1, N'Americano', 49000,'M', 'src\\image\\Product_image\\Americano.png'),
(2, N'Americano', 55000, 'L', 'src\\image\\Product_image\\Americano.png'),
(3, N'Espresso', 49000, 'M', 'src\\image\\Product_image\\Espresso.png'),
(4, N'Espresso', 55000, 'L', 'src\\image\\Product_image\\Espresso.png'),
(5, N'Caramel Macchiato', 79000,'M', 'src\\image\\Product_image\\Caramel Macchiato.png'),
(6, N'Caramel Macchiato', 85000, 'L', 'src\\image\\Product_image\\Caramel Macchiato.png'),
(7, N'Matcha Macchiato', 79000, 'M', 'src\\image\\Product_image\\Matcha Macchiato MD.png'),
(8, N'Matcha Macchiato', 85000, 'L', 'src\\image\\Product_image\\Matcha Macchiato LG.png'),
(9, N'Latte', 75000, 'M', 'src\\image\\Product_image\\Latte.png'),
(10, N'Latte', 79000, 'L', 'src\\image\\Product_image\\Latte.png'),
(11, N'Cappuchino', 75000, 'M','src\\image\\Product_image\\Cappuchino.png'),
(12, N'Cappuchino', 79000, 'L', 'src\\image\\Product_image\\Cappuchino.png'),
(13, N'Cold Brew', 69000, 'M', 'src\\image\\Product_image\\Cold Brew.png'),
(14, N'Cold Brew', 79000, 'L', 'src\\image\\Product_image\\Cold Brew.png'),
(15, N'Matcha Latte', 49000,'M', 'src\\image\\Product_image\\Matcha Latte.png'),
(16, N'Matcha Latte', 55000, 'L', 'src\\image\\Product_image\\Matcha Latt.png'),
(17, N'Trà Thạch Vải', 55000, 'M', N'src\\image\\Product_image\\Trà Thạch Vải.png'),
(18, N'Trà Thạch Vải', 65000, 'L', N'src\\image\\Product_image\\Trà Thạch Vải.png'),
(19, N'Trà Thanh Đào', 55000, 'M',N'src\\image\\Product_image\\Trà Thanh Đào.png'),
(20, N'Trà Thanh Đào', 65000, 'L', N'src\\image\\Product_image\\Trà Thanh Đào.png'),
(21, N'Trà Sen Vàng', 55000,'M', N'src\\image\\Product_image\\Trà Sen Vàng.png'),
(22, N'Trà Sen Vàng', 65000,'L', N'src\\image\\Product_image\\Trà Sen Vàng.png'),
(23, N'Trà Xanh Đậu Đỏ', 55000,'M', N'src\\image\\Product_image\\Trà Xanh Đậu Đỏ.png'),
(24, N'Trà Xanh Đậu Đỏ', 65000,'L', N'src\\image\\Product_image\\Trà Xanh Đậu Đỏ.png'),
(25, N'Bánh Croissant', 29000,'M', N'src\\image\\Product_image\\Bánh Croissant.png'),
(26, N'Bánh Mì Que Bò Sốt Phô Mai', 19000,'M', N'src\\image\\Product_image\\Bánh Mì Que Bò Sốt Phô Mai.png'),
(27, N'Bánh Mì Que Gà Sốt Phô Mai', 29000,'L', N'src\\image\\Product_image\\Bánh Mì Que Gà Sốt Phô Mai.png'),
(28, N'Bánh Mousse Đào', 35000,'M', N'src\\image\\Product_image\\Bánh Mousse Đào.png'),
(29, N'Bánh Mousse CaCao', 35000,'M', N'src\\image\\Product_image\\Bánh Mousse CaCao.png'),
(30, N'Bánh Taramisu', 35000,'M', N'src\\image\\Product_image\\Bánh Taramisu.png'),
(31, N'Bánh Chuối', 35000,'M', N'src\\image\\Product_image\\Bánh Chuối.png');

DELETE FROM ProductIngredient
DELETE FROM OrderDetail
DELETE FROM [dbo].[Product]

--TableCaffe
INSERT INTO TableCaffe (TableID, status, tableName) VALUES
(1, N'Trống', N'Bàn 1'),
(2, N'Trống', N'Bàn 2'),
(3, N'Trống', N'Bàn 3'),
(4, N'Trống', N'Bàn 4'),
(5, N'Trống', N'Bàn 5'),
(6, N'Trống', N'Bàn 6'),
(7, N'Trống', N'Bàn 7'),
(8, N'Trống', N'Bàn 8'),
(9, N'Trống', N'Bàn 9'),
(10, N'Trống', N'Bàn 10'),
(11, N'Trống', N'Bàn 11'),
(12, N'Trống', N'Bàn 12'),
(13, N'Trống', N'Bàn 13'),
(14, N'Trống', N'Bàn 14'),
(15, N'Trống', N'Bàn 15'),
(16, N'Trống', N'Bàn 16'),
(17, N'Trống', N'Bàn 17'),
(18, N'Trống', N'Bàn 18'),
(19, N'Trống', N'Bàn 19'),
(20, N'Trống', N'Bàn 20'),
(21, N'Trống', N'Bàn 21'),
(22, N'Trống', N'Bàn 22'),
(23, N'Trống', N'Bàn 23'),
(24, N'Trống', N'Bàn 24'),
(25, N'Trống', N'Bàn 25'),
(26, N'Trống', N'Bàn 26'),
(27, N'Trống', N'Bàn 27'),
(28, N'Trống', N'Bàn 28'),
(29, N'Trống', N'Bàn 29'),
(30, N'Trống', N'Bàn 30');
-- Có bàn có khách thì ghi N'Có khách'

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

-- ProductIngredient
INSERT INTO ProductIngredient (productID, ingredientID, quantity, unit) VALUES				
(1, 1, 50, N'gram'), -- Americano
(2, 1, 60, N'gram'),

(3, 1, 30, N'gram'), -- Espresso
(4, 1, 40, N'gram'),

(5, 1, 40, N'gram'), -- Caramel Macchiato
(5, 2, 100, N'ml'),
(5, 4, 20, N'ml'),
(6, 1, 50, N'gram'),
(6, 2, 120, N'ml'),
(6, 4, 30, N'ml'),

(7, 7, 30, N'gram'), -- Matcha Macchiato
(7, 2, 150, N'ml'),
(8, 7, 40, N'gram'),
(8, 2, 180, N'ml'),

(9, 1, 30, N'gram'), -- Latte
(9, 2, 120, N'ml'),
(10, 1, 40, N'gram'),
(10, 2, 150, N'ml'),

(11, 1, 40, N'gram'), -- Cappuccino
(11, 2, 120, N'ml'),
(12, 1, 50, N'gram'),
(12, 2, 150, N'ml'),

(13, 1, 50, N'gram'), -- Cold Brew
(13, 12, 100, N'gram'),
(14, 1, 60, N'gram'),
(14, 12, 120, N'gram'),

(15, 7, 30, N'gram'), -- Matcha Latte
(15, 2, 120, N'ml'),
(16, 7, 40, N'gram'),
(16, 2, 150, N'ml'),

(17, 6, 30, N'ml'), -- Trà Thạch Vải
(17, 10, 40, N'gram'),
(18, 6, 40, N'ml'),
(18, 10, 50, N'gram'),

(19, 5, 30, N'ml'), -- Trà Thanh Đào
(19, 9, 40, N'gram'),
(20, 5, 40, N'ml'),
(20, 9, 50, N'gram'),

(21, 10, 40, N'gram'), -- Trà Sen Vàng
(22, 10, 50, N'gram'),


(23, 11, 40, N'gram'), -- Trà Xanh Đậu Đỏ
(24, 11, 50, N'gram'),

(25, 14, 100, N'gram'), -- Bánh Croissant
(25, 15, 50, N'gram'),

(26, 14, 80, N'gram'), -- Bánh Mì Que
(26, 15, 30, N'gram'),
(26, 16, 1, N'quả'),
(26, 17, 30, N'gram'),

(28, 14, 80, N'gram'), -- Bánh Mousse Đào
(28, 15, 30, N'gram'),
(28, 5, 20, N'ml'),

(29, 14, 80, N'gram'), -- Bánh Mousse CaCao
(29, 15, 30, N'gram'),
(29, 13, 20, N'gram'),

(30, 14, 80, N'gram'), -- Bánh Tiramisu
(30, 15, 30, N'gram'),
(30, 13, 20, N'gram'),

(31, 14, 80, N'gram'), -- Bánh Chuối
(31, 15, 30, N'gram'),
(31, 22, 1, N'Trái');

-- Import
INSERT INTO Import (importID, ingredientID, quantity, unitPrice, importDate, totalCost) 
VALUES
(1, 1, 500, 0.50, '2025-03-24 08:30:00', 250.00),
(2, 2, 1000, 0.20, '2025-03-24 09:00:00', 200.00),
(3, 3, 800, 0.25, '2025-03-24 09:30:00', 200.00),
(4, 4, 500, 0.30, '2025-03-24 10:00:00', 150.00),
(5, 5, 400, 0.35, '2025-03-24 10:30:00', 140.00),
(6, 6, 600, 0.40, '2025-03-24 11:00:00', 240.00),
(7, 7, 700, 0.45, '2025-03-24 11:30:00', 315.00),
(8, 8, 600, 0.50, '2025-03-24 12:00:00', 300.00),
(9, 9, 400, 0.55, '2025-03-24 12:30:00', 220.00),
(10, 10, 350, 0.60, '2025-03-24 13:00:00', 210.00),
(11, 11, 500, 0.65, '2025-03-24 13:30:00', 325.00),
(12, 12, 1500, 0.10, '2025-03-24 14:00:00', 150.00),
(13, 13, 900, 0.55, '2025-03-24 14:30:00', 495.00),
(14, 14, 1000, 0.30, '2025-03-24 15:00:00', 300.00),
(15, 15, 600, 0.75, '2025-03-24 15:30:00', 450.00),
(16, 16, 500, 0.50, '2025-03-24 16:00:00', 250.00),
(17, 17, 550, 0.80, '2025-03-24 16:30:00', 440.00),
(18, 20, 1200, 0.20, '2025-03-24 17:00:00', 240.00),
(19, 21, 700, 0.90, '2025-03-24 17:30:00', 630.00),
(20, 22, 100, 1.00, '2025-03-24 18:00:00', 100.00);

-- Order
INSERT INTO Orders (orderID, tableID, employeeID, customerID, orderTime, totalPrice, [status]) 
VALUES
(1, 1, 100004, 100000, '2025-03-24 08:45:00', 49000.00, N'Đã thanh toán'),  -- status sẽ nhận các giá trị: Đang chuẩn bị , Hoàn thành, Đã thanh toán
(2, 2, 100004, 100000, '2025-03-24 09:15:00', 55000.00, N'Đã thanh toán'),
(3, 3, 100005, 100000, '2025-03-24 09:30:00', 49000.00, N'Đã thanh toán'),
(4, 4, 100005, 100000, '2025-03-24 10:00:00', 55000.00, N'Đã thanh toán');

-- OrderDetail
INSERT INTO OrderDetail (orderDetailID, orderID, productID, quantity, price) 
VALUES
(1, 1, 1, 1, 49000.00),
(2, 2, 2, 1, 55000.00),
(3, 3, 3, 1, 49000.00),
(4, 4, 4, 1, 55000.00);

-- Payment
INSERT INTO Payment (paymentID, orderID, paymentMethod, amount, paymentTime) 
VALUES
(1, 1, N'Tiền mặt', 49000.00, '2025-03-24 09:00:00'),
(2, 2, N'Thẻ tín dụng', 55000.00, '2025-03-24 09:20:00'),
(3, 3, N'Ví điện tử', 49000.00, '2025-03-24 09:45:00'),
(4, 4, N'Tiền mặt', 55000.00, '2025-03-24 10:15:00');
