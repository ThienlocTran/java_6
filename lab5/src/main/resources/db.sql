CREATE DATABASE J6_Lab5;
GO
USE J6_Lab5;
GO

CREATE TABLE J6Students(
                           Id VARCHAR(50) NOT NULL PRIMARY KEY,
                           Name NVARCHAR(50) NOT NULL,
                           Mark FLOAT NOT NULL,
                           Gender BIT NOT NULL
);

INSERT INTO J6Students VALUES('SV001', N'Lý Thái Tổ', 9.5, 1);
INSERT INTO J6Students VALUES('SV002', N'Lê Trọng Tấn', 4.5, 1);
INSERT INTO J6Students VALUES('SV003', N'Nguyễn Thị Minh Khai', 9.5, 0);
INSERT INTO J6Students VALUES('SV004', N'Đoàn Trung Trực', 6.0, 1);
