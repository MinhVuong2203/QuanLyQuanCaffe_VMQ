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
    hourWage int,
	CCCD CHAR(12) NOT NULL CHECK (CCCD LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
	birthDate DATE,
	gender NVARCHAR(10) CHECK (gender IN (N'Nam', N'Nữ')),
	[image] NVARCHAR(255)
)

CREATE TABLE Customer(
    customerID int PRIMARY KEY,
    name nvarchar(50),
    phone char(10) NULL CHECK (phone LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    point int NOT NULL CHECK(point>=0)
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

CREATE TABLE OrderDetail(
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

GO
-- Dùng trigger để tự động lấy lương của nhân viên, và tính tiền lương theo ca
go
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
INSERT INTO UserAccount (ID, username, password, role) VALUES
(100001, 'quy123', 'FTQft1u8HZnOHAjCOVH13MCPokjIOIGKs1Gpm5Jqgag=', N'Quản lí'),
(100002, 'minh123', 'RAycCtCJYS9iHENtq77sNQHSh/b3GosUgFxfc2OmzlI=', N'Quản lí'),
(100003, 'vuong123', '6gS5n5kVDOUZ3xqtOqOwHpKJ65m4Q87KVY/JhGpwUZ8=', N'Quản lí'),
(100004, 'duypham123', 'xpXHU8l//rn51AqG5/dVU+EV0JogOcj3TxUE04j61rI=', N'thu ngân'),
(100005, 'maitrinh123', '2Y9HhBICzk7iWG8502bDsiK7rLBpgD8hOhLasONhL7Y=', N'thu ngân'),
(100006, 'trangngo12', 'KDvuXso43zWo3Yda9Jc2gkMDUKrzd+0p7ta8c0O4MZ8=', N'pha chế'),
(100007, 'lanvu123', 'XyxbQvbbzV8b0pq89FmvZZ4YRcfhcM1Oks4ckBiI2+g=', N'phục vụ'),
(100008, 'hoang123', 'xXz4PwWcUCfIkgI4Cih6DaREk1vSIwCoAH3e3CnaT18=', N'thu ngân'),
(100009, 'thanh123', 'CeESODFm5wP9G8MFQjiMMEMJ32KCoh5rrmOLCHQmRfo=', N'thu ngân'),
(100010, 'linh123', '797rdUtaT8uXEVRqbRmCsmMhfxi0C5Wv+QoPe8HAf9M=', N'thu ngân'),
(100011, 'nam1234', 'WrRhSUfoZtbJippliv4QkJ3HTXDmhNQ6EPvregdkA2c=', N'pha chế'),
(100012, 'hien123', 'az5TUjq7GnULbEFHqdPQhVCZPlEisuKO1+OK1KuaOU0=', N'pha chế'),
(100013, 'bao1234', 'ofJBCx09Zpu83nsYA1t2i0nxY9UYNZU0R9RMqCw8u54=', N'pha chế'),
(100014, 'anh1234', 'b6kkYdT8Jhwiy7JjvoXQsjjrKI4AUNWml3UvkLRML9I=', N'pha chế'),
(100015, 'tuan123', 'YBzjroEkmpnlpvrrX/XlDbn0aSXGzgjiEpmOfXREdKo=', N'phục vụ'),
(100016, 'mai1234', 'jrtHoeBSjFU25drSeDaeCh+OJW08DN1bYu1xQ++WGCg=', N'phục vụ'),
(100017, 'huy1234', 'M9O2ubw5yB/ITy+oE55PDXq1Izx/6pu3uuIMAU+mZC4=', N'phục vụ'),
(100018, 'nga1234', 'pPD8F8150T4NTG8JARFc2BNqeN1vRa91bYBhd0PTVNs=', N'phục vụ'),
(100019, 'khoa123', 'eylUcgwkgqu1rLFWApiV1ck4D8x+83k6q5NzMzhpMuw=', N'phục vụ'),
(100020, 'phuc123', '+q3FjeeOBUtua31Y+uRMXEz0zumgvFXnTfyARKNpYag=', N'phục vụ'),
(100021, 'tam1234', 'j7lm41E6uPiyUKgzwSRPPn4t6ffJum8gRTtf94rGhpw=', N'phục vụ'),
(100022, 'yen1234', 'TwfM+L5CmTwS2ZsX5nzQpzUbxOaAVZDVXQob2CDaIT8=', N'phục vụ'),
(100023, 'lam1234', 'gEhwYX6w0K5HQdTAaBKzCaV5dz/42BKZ9+XLtB3EzlI=', N'phục vụ'),
(100024, 'dung123', 'zvsnGiZVOT9088XY0kDtMuXxb7pSdcrF2d/a6iEuJAM=', N'phục vụ'),
(100025, 'nhi1234', 'hnh3I65sWxs+xAm4Opppj/LPwENk9E+j4nW17lx8U04=', N'phục vụ'),
(100026, 'quang123', 'G16EM32yrrF6U1MhNH3skITfiDrtwKHedFkznDw+z/Y=', N'phục vụ'),
(100027, 'thu1234', 'VcGL6wdv0SfrTf2K5eDB4y/mJP/8x1BBazFaIEIRxfg=', N'phục vụ');

--Employee
INSERT INTO Employee (employeeID, name, phone, hourWage, CCCD, birthDate, gender, image)
VALUES
(100001, N'Nguyễn Ngọc Quý', '0912345678', '', '012345678901', '1990-05-12', N'Nam', N'src\\image\\Manager_Image\\Manager_Defaut.png'),
(100002, N'Nguyễn Bình Minh', '0923456789', '', '023456789012', '1992-07-25', N'Nam', N'src\\image\\Manager_Image\\Manager_Defaut.png'),
(100003, N'Nguyễn Minh Vương', '0818214849', '', '034567890123', '1988-03-18', N'Nam', N'src\\image\\Manager_Image\\Manager_Defaut.png'),
(100004, N'Phạm Duy', '0945678901', 40000, '045678901234', '1995-11-02', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100005, N'Mai Trinh', '0956789012', 40000, '056789012345', '1997-09-14', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100006, N'Ngô Trang', '0967890123', 45000, '067890123456', '2000-12-22', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100007, N'Vũ Lan', '0978901234', 32000, '078901234567', '1999-06-30', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100008, N'Nguyễn Hoàng', '0989012345', 40000, '089012345678', '1996-04-10', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100009, N'Trần Thanh', '0990123456', 40000, '090123456789', '1998-08-15', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100010, N'Phạm Linh', '0901234567', 40000, '091234567890', '1997-02-20', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100011, N'Lê Nam', '0912345670', 45000, '092345678901', '1999-10-05', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100012, N'Nguyễn Hiền', '0923456780', 45000, '093456789012', '2000-03-12', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100013, N'Trần Bảo', '0934567890', 45000, '094567890123', '1998-07-25', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100014, N'Võ Anh', '0945678902', 45000, '095678901234', '2001-01-30', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100015, N'Nguyễn Tuấn', '0956789013', 32000, '096789012345', '2000-06-18', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100016, N'Phạm Mai', '0967890124', 32000, '097890123456', '1999-09-22', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100017, N'Lê Huy', '0978901235', 32000, '098901234567', '2001-11-14', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100018, N'Trần Nga', '0989012346', 32000, '099012345678', '2000-02-28', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100019, N'Vũ Khoa', '0990123457', 32000, '100123456789', '1998-12-10', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100020, N'Ngô Phúc', '0901234568', 32000, '101234567890', '1999-04-05', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100021, N'Hoàng Tâm', '0912345671', 32000, '102345678901', '2000-07-19', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100022, N'Nguyễn Yến', '0923456781', 32000, '103456789012', '2001-03-03', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100023, N'Trần Lâm', '0934567891', 32000, '104567890123', '1999-08-27', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100024, N'Phạm Dũng', '0945678903', 32000, '105678901234', '2000-05-15', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100025, N'Lê Nhi', '0956789014', 32000, '106789012345', '2001-10-08', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png'),
(100026, N'Võ Quang', '0967890125', 32000, '107890123456', '1998-06-12', N'Nam', N'src\\image\\Employee_Image\\Employee_default.png'),
(100027, N'Nguyễn Thu', '0978901236', 32000, '108901234567', '2000-01-25', N'Nữ', N'src\\image\\Employee_Image\\Employee_default.png');


-- Customer
INSERT INTO [dbo].[Customer]([customerID],[name],[phone],[point])   -- Để phục vụ cho khách vãng lai (khách nào vãng lai thì lấy ID này, nhân viên có chức năng tạo khách vãng lai bằng một nút bấm cho thao tác nhanh hơn, không cần nhập ID khách)
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

INSERT INTO EmployeeShift (shiftID,employeeID, startTime, endTime)
VALUES 
(100, 100004,'2025-04-01 08:00:00', '2025-04-01 12:00:00'),
(101, 100005 ,'2025-04-02 14:00:00', '2025-04-02 18:00:00'),
(102, 100006,'2025-04-01 09:00:00', '2025-04-01 15:00:00'),
(103, 100007,'2025-04-03 07:00:00', '2025-04-03 11:00:00'),
(104, 100005,'2025-04-02 10:00:00', '2025-04-02 14:00:00'),
(105, 100004,'2025-04-03 11:00:00', '2025-04-03 17:00:00')



-- Xóa tất cả các bảng và chạy lại các dòng trên
-- Tắt kiểm tra khóa ngoại
EXEC sp_msforeachtable "ALTER TABLE ? NOCHECK CONSTRAINT ALL"
-- Xóa toàn bộ bảng
EXEC sp_msforeachtable "DROP TABLE ?"
