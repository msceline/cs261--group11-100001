-- ==============================================================================
-- File: user.sql
-- Description: โครงสร้างตาราง users และข้อมูลจำลอง (Sprint 1)
-- Target DBMS: Microsoft SQL Server 2022
-- ==============================================================================

USE [master];
GO

-- ==============================================================================
-- [ส่วนที่ 1] โครงสร้างตาราง (DDL Reference)
-- ==============================================================================
/*
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
BEGIN
    CREATE TABLE [users] (
        [user_id] INT IDENTITY(1,1) PRIMARY KEY,
        [tu_id] VARCHAR(20) NOT NULL UNIQUE,          -- รหัสนักศึกษา / รหัสบุคลากร
        [role] VARCHAR(20) NOT NULL DEFAULT 'student', -- 'student', 'teacher'
        [display_name] NVARCHAR(100) NOT NULL,
        [email] VARCHAR(100) NOT NULL UNIQUE,
        [password] VARCHAR(255) NULL,                  -- ว่างได้ถ้าล็อกอินผ่าน TU API
        [created_at] DATETIME2 DEFAULT GETDATE()
    );
END
GO
*/

-- ==============================================================================
-- [ส่วนที่ 2] ข้อมูลเริ่มต้นจำลอง (Mock / Seed Data)
-- *รหัสผ่านจำลองของ user คือ "password123" (SHA-256)
-- ค่า Hash: ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f
-- ==============================================================================

-- นักศึกษา 1 (มีรหัสผ่าน)
IF NOT EXISTS (SELECT 1 FROM [users] WHERE [tu_id] = '6609650001')
BEGIN
    INSERT INTO [users] ([tu_id], [role], [display_name], [email], [password], [created_at])
    VALUES (
        '6609650001',
        'student',
        N'Somchai Jaidee',
        'somchai.jai@dome.tu.ac.th',
        'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f',
        '2026-01-10 09:00:00'
    );
END
GO

-- นักศึกษา 2 (มีรหัสผ่าน)
IF NOT EXISTS (SELECT 1 FROM [users] WHERE [tu_id] = '6609650002')
BEGIN
    INSERT INTO [users] ([tu_id], [role], [display_name], [email], [password], [created_at])
    VALUES (
        '6609650002',
        'student',
        N'Somying Rakdee',
        'somying.rak@dome.tu.ac.th',
        'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f',
        '2026-01-11 10:30:00'
    );
END
GO

-- นักศึกษา 3 (ล็อกอินผ่าน TU API -> password เป็น NULL)
IF NOT EXISTS (SELECT 1 FROM [users] WHERE [tu_id] = '6609650003')
BEGIN
    INSERT INTO [users] ([tu_id], [role], [display_name], [email], [password], [created_at])
    VALUES (
        '6609650003',
        'student',
        N'Somchart Meesuk',
        'somchart.mee@dome.tu.ac.th',
        NULL,
        '2026-01-12 14:15:00'
    );
END
GO

-- อาจารย์ผู้สอน (Teacher)
IF NOT EXISTS (SELECT 1 FROM [users] WHERE [tu_id] = 'T00001')
BEGIN
    INSERT INTO [users] ([tu_id], [role], [display_name], [email], [password], [created_at])
    VALUES (
        'T00001',
        'teacher',
        N'Dr. Kanya Srichan',
        'kanya.sri@tu.ac.th',
        NULL,
        '2026-01-05 08:30:00'
    );
END
GO

-- ==============================================================================
-- [ส่วนที่ 3] ตรวจสอบข้อมูล
-- ==============================================================================
SELECT 
    [user_id], 
    [tu_id], 
    [role],
    [display_name], 
    [email], 
    CASE WHEN [password] IS NULL THEN '(Login via TU API)' ELSE '(Password Set)' END AS auth_method,
    [created_at]
FROM [users];
GO
