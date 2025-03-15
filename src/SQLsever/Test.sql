
CREATE TABLE UserSystem(
	id int PRIMARY KEY,
	name nvarchar(50) NOT NULL,
	phone char(10) NOT NULL,
	username char(20) NOT NULL,
	password char(20) NOT NULL,
	role nvarchar(50) NOT NULL
)

DELETE FROM [dbo].[UserSystem]

INSERT INTO [dbo].[UserSystem]
VALUES  (10001, N'Nguyễn Minh Vương', '0818214849', 'Mvuongggg', 'hihihihihi', N'Quản lí'),
		(10002, N'Bình Minh', '0123456789', 'binhminh', 'binhminh234', N'Quản lí'),
		(10003, N'Phú Quý', '0987654321', 'phuquy2345d', 'phuquy2345', N'Quản lí')

