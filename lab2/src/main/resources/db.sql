-- 1. XÓA BẢNG CŨ (Để tránh lỗi bảng đã tồn tại)
DROP TABLE IF EXISTS UserRoles;
DROP TABLE IF EXISTS Authorities;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Roles;

-- 2. TẠO CẤU TRÚC BẢNG (Thứ tự: Cha trước - Con sau)
CREATE TABLE Users (
                       Username VARCHAR(50) NOT NULL PRIMARY KEY,
                       Password VARCHAR(500) NOT NULL,
                       Enabled BIT NOT NULL
);

-- Dùng cho Bài 2 (JDBC mặc định)
CREATE TABLE Authorities (
                             Id BIGINT NOT NULL IDENTITY(1, 1) PRIMARY KEY,
                             Username VARCHAR(50) NOT NULL,
                             Authority VARCHAR(50) NOT NULL,
                             UNIQUE(Username, Authority),
                             FOREIGN KEY(Username) REFERENCES Users (Username) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Dùng cho Bài 3 (Custom DAO)
CREATE TABLE Roles (
                       Id VARCHAR(50) NOT NULL PRIMARY KEY,
                       Name NVARCHAR(50) NOT NULL
);

CREATE TABLE UserRoles (
                           Id BIGINT NOT NULL IDENTITY(1, 1) PRIMARY KEY,
                           Username VARCHAR(50) NOT NULL,
                           RoleId VARCHAR(50) NOT NULL,
                           CONSTRAINT FK_User FOREIGN KEY (Username) REFERENCES Users(Username),
                           CONSTRAINT FK_Role FOREIGN KEY (RoleId) REFERENCES Roles(Id)
);

-- 3. NHẬP DỮ LIỆU MẪU (Thứ tự quan trọng)

-- Bước A: Nhập Users trước
INSERT INTO Users(Username, Password, Enabled) VALUES('user@gmail.com', '{noop}123', 1);
INSERT INTO Users(Username, Password, Enabled) VALUES('admin@gmail.com', '{noop}123', 1);
INSERT INTO Users(Username, Password, Enabled) VALUES('both@gmail.com', '{noop}123', 1);

-- Bước B: Nhập Roles
INSERT INTO Roles(Id, Name) VALUES('ROLE_USER', N'Nhân viên');
INSERT INTO Roles(Id, Name) VALUES('ROLE_ADMIN', N'Quản lý');

-- Bước C: Nhập Authorities (Cho Bài 2)
INSERT INTO Authorities(Username, Authority) VALUES('user@gmail.com', 'ROLE_USER');
INSERT INTO Authorities(Username, Authority) VALUES('admin@gmail.com', 'ROLE_ADMIN');