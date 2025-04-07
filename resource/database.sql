
CREATE TABLE UserAccount(
    ID int PRIMARY KEY,
    username varchar(100) NOT NULL,
    [password] varchar(100) NOT NULL,
    role nvarchar(50) NOT NULL
)

CREATE TABLE Employee(
    employeeID int NOT NULL,
    foreign key (employeeID) references UserAccount(ID),
	PRIMARY KEY (employeeID),
    [name] nvarchar(50) NOT NULL,
    phone char(10) NOT NULL CHECK (phone LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    hourWage int NOT NULL,
	CCCD CHAR(12) NOT NULL CHECK (CCCD LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
	birthDate DATE NOT NULL,
	gender NVARCHAR(10) NOT NULL CHECK (gender IN (N'Nam', N'Nữ')),
	[image] NVARCHAR(255)
)

ALTER TABLE Employee
ADD [image] NVARCHAR(255)
ALTER TABLE Customer
ADD [image] NVARCHAR(255)



CREATE TABLE Customer(
    customerID int NOT NULL,
	foreign key (customerID) references UserAccount(ID),
	PRIMARY KEY (customerID),
    name nvarchar(50) NOT NULL,
    phone char(10) NULL CHECK (phone LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    point int NOT NULL CHECK(point>=0),
	[image] NVARCHAR(255)
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
    orderID int NOT NULL,
    productID int NOT NULL,
    quantity int NOT NULL CHECK (quantity > 0),
    price decimal(10,2) NOT NULL,
    foreign key (orderID) references Orders(orderID),
    foreign key (productID) references [Product](productID),
	PRIMARY KEY (orderID, productID)
)
CREATE TABLE Payment(
	paymentID int PRIMARY KEY,
	orderID int NOT NULL,
	paymentMethod nvarchar(50) NOT NULL,
	amount decimal(10,2) NOT NULL,
	paymentTime datetime NOT NULL,
)

DROP TABLE EmployeeShift
CREATE TABLE EmployeeShift (
    shiftID INT PRIMARY KEY,
    employeeID INT NOT NULL,
    startTime DATETIME NOT NULL,
    endTime DATETIME NOT NULL,
    hourWorked AS DATEDIFF(MINUTE, startTime, endTime) / 60.0 PERSISTED,
    salary DECIMAL(10,2) NULL,
	status NVARCHAR(50) DEFAULT N'chưa điểm danh',
    FOREIGN KEY (employeeID) REFERENCES Employee(employeeID)
);

delete from EmployeeShift

INSERT INTO EmployeeShift (shiftID,employeeID, startTime, endTime)
VALUES 
(100, 100004,'2025-04-01 08:00:00', '2025-04-01 12:00:00'),
(101, 100005 ,'2025-04-02 14:00:00', '2025-04-02 18:00:00'),
(102, 100006,'2025-04-01 09:00:00', '2025-04-01 15:00:00'),
(103, 100007,'2025-04-03 07:00:00', '2025-04-03 11:00:00'),
(104, 100005,'2025-04-02 10:00:00', '2025-04-02 14:00:00');
INSERT INTO EmployeeShift (shiftID,employeeID, startTime, endTime)
VALUES 
(105, 100004,'2025-04-03 11:00:00', '2025-04-03 17:00:00')

ALTER TABLE EmployeeShift
ADD status NVARCHAR(50) DEFAULT N'chưa điểm danh';


go
-- Dùng trigger để tự động lấy lương của nhân viên, và tính tiền lương theo ca
CREATE TRIGGER trg_CalculateSalary
ON EmployeeShift
AFTER INSERT, UPDATE
AS
BEGIN
    UPDATE es
    SET es.salary = es.hourWorked * e.hourWage
    FROM EmployeeShift es
    JOIN Employee e ON es.employeeID = e.employeeID
    WHERE es.salary IS NULL;
END;

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
INSERT INTO Employee (employeeID, name, phone, hourWage, CCCD, birthDate, gender, image)
VALUES
(100001, N'Nguyễn Ngọc Quý', '0912345678', 0, '012345678901', '1990-05-12', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100002, N'Nguyễn Bình Minh', '0923456789', 0, '023456789012', '1992-07-25', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100003, N'Nguyễn Minh Vương', '0818214849', 0, '034567890123', '1988-03-18', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100004, N'Phạm Duy', '0945678901', 40000, '045678901234', '1995-11-02', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100005, N'Mai Trinh', '0956789012', 40000, '056789012345', '1997-09-14', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100006, N'Ngô Trang', '0967890123', 45000, '067890123456', '2000-12-22', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100007, N'Vũ Lan', '0978901234', 32000, '078901234567', '1999-06-30', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png');

-- Customer
INSERT INTO [dbo].[Customer]([customerID],[name],[phone],[point])
VALUES (100000, N'Khách vãng lai', '0000000000',0)

--Product
INSERT INTO Product (ProductID, name, price,size, image) VALUES
-- Coffee (categoryID = 1)
(1, N'Americano', 49000,'M', N'src\\image\\Product_image\\Americano.png'),
(2, N'Americano', 55000, 'L', N'src\\image\\Product_image\\Americano.png'),
(3, N'Espresso', 49000, 'M', N'src\\image\\Product_image\\Espresso.png'),
(4, N'Espresso', 55000, 'L', N'src\\image\\Product_image\\Espresso.png'),
(5, N'Caramel Macchiato', 79000,'M', N'src\\image\\Product_image\\Caramel Macchiato.png'),
(6, N'Caramel Macchiato', 85000, 'L', N'src\\image\\Product_image\\Caramel Macchiato.png'),
(7, N'Moccha Macchiato', 79000, 'M', N'src\\image\\Product_image\\Moccha Macchiato.png'),
(8, N'Moccha Macchiato', 85000, 'L', N'src\\image\\Product_image\\Moccha Macchiato.png'),
(9, N'Latte', 75000, 'M', N'src\\image\\Product_image\\Latte.png'),
(10, N'Latte', 79000, 'L', N'src\\image\\Product_image\\Latte.png'),
(11, N'Cappuchino', 75000, 'M',N'src\\image\\Product_image\\Cappuchino.png'),
(12, N'Cappuchino', 79000, 'L', N'src\\image\\Product_image\\Cappuchino.png'),
(13, N'Cold Brew', 69000, 'M', N'src\\image\\Product_image\\Cold Brew.png'),
(14, N'Cold Brew', 79000, 'L', N'src\\image\\Product_image\\Cold Brew.png'),
(15, N'Matcha Latte', 49000,'M', N'src\\image\\Product_image\\Matcha Latte.png'),
(16, N'Matcha Latte', 55000, 'L', N'src\\image\\Product_image\\Matcha Latte.png'),
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

-- Order
INSERT INTO Orders (orderID, tableID, employeeID, customerID, orderTime, totalPrice, [status]) 
VALUES
(1, 1, 100004, 100000, '2025-03-24 08:45:00', 49000.00, N'Đã thanh toán'),  -- status sẽ nhận các giá trị: Đang chuẩn bị , Hoàn thành, Đã thanh toán
(2, 2, 100004, 100000, '2025-03-24 09:15:00', 55000.00, N'Đã thanh toán'),
(3, 3, 100005, 100000, '2025-03-24 09:30:00', 49000.00, N'Đã thanh toán'),
(4, 4, 100005, 100000, '2025-03-24 10:00:00', 55000.00, N'Đã thanh toán');

DROP TABLE OrderDetail

-- OrderDetail
INSERT INTO OrderDetail (orderID, productID, quantity, price) 
VALUES
(1, 1, 1, 49000.00),
(1, 2, 1, 55000.00),
(3, 3, 1, 49000.00),
(3, 4, 1, 55000.00);

-- Payment
INSERT INTO Payment (paymentID, orderID, paymentMethod, amount, paymentTime) 
VALUES
(1, 1, N'Tiền mặt', 49000.00, '2025-03-24 09:00:00'),
(2, 2, N'Thẻ tín dụng', 55000.00, '2025-03-24 09:20:00'),
(3, 3, N'Ví điện tử', 49000.00, '2025-03-24 09:45:00'),
(4, 4, N'Tiền mặt', 55000.00, '2025-03-24 10:15:00');


-- password nè :))
--  Minh123@
--  Quy1234@
--	Vuong123@

-- Khách
-- 16: taiNguyen1

-- 04: duyPham123       pass: pass1234@           hashpass: xpXHU8l//rn51AqG5/dVU+EV0JogOcj3TxUE04j61rI=