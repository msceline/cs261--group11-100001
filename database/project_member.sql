-- ==============================================================================
-- File: project_member.sql
-- Description: โครงสร้างตาราง project_members และข้อมูลจำลอง (Sprint 1)
-- Target DBMS: Microsoft SQL Server 2022
-- ==============================================================================

USE [master];
GO

-- ==============================================================================
-- [ส่วนที่ 1] โครงสร้างตาราง (DDL Reference)
-- ==============================================================================
/*
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='project_members' AND xtype='U')
BEGIN
    CREATE TABLE [project_members] (
        [id] INT IDENTITY(1,1) PRIMARY KEY,
        [user_id] INT NOT NULL,
        [project_id] INT NOT NULL,
        [role_in_project] VARCHAR(20) NOT NULL DEFAULT 'member', -- 'host', 'member', 'visitor'
        [joined_at] DATETIME2 DEFAULT GETDATE(),
        CONSTRAINT FK_ProjectMember_User FOREIGN KEY ([user_id]) REFERENCES [users]([user_id]),
        CONSTRAINT FK_ProjectMember_Project FOREIGN KEY ([project_id]) REFERENCES [projects]([project_id]),
        CONSTRAINT UQ_Project_User UNIQUE ([project_id], [user_id])
    );
END
GO
*/

-- ==============================================================================
-- [ส่วนที่ 2] ข้อมูลเริ่มต้นจำลอง (Mock / Seed Data)
-- ==============================================================================

-- โปรเจกต์ที่ 1: TU-WorkSpace-Group11
-- User 1 เป็น host, User 2 เป็น member, User 3 เป็น member, User 4 (อาจารย์) เป็น visitor
IF NOT EXISTS (SELECT 1 FROM [project_members] WHERE [project_id] = 1 AND [user_id] = 1)
BEGIN
    INSERT INTO [project_members] ([project_id], [user_id], [role_in_project], [joined_at])
    VALUES (1, 1, 'host', '2026-02-01 10:00:00');
END
GO

IF NOT EXISTS (SELECT 1 FROM [project_members] WHERE [project_id] = 1 AND [user_id] = 2)
BEGIN
    INSERT INTO [project_members] ([project_id], [user_id], [role_in_project], [joined_at])
    VALUES (1, 2, 'member', '2026-02-01 10:15:00');
END
GO

IF NOT EXISTS (SELECT 1 FROM [project_members] WHERE [project_id] = 1 AND [user_id] = 3)
BEGIN
    INSERT INTO [project_members] ([project_id], [user_id], [role_in_project], [joined_at])
    VALUES (1, 3, 'member', '2026-02-01 10:30:00');
END
GO

IF NOT EXISTS (SELECT 1 FROM [project_members] WHERE [project_id] = 1 AND [user_id] = 4)
BEGIN
    INSERT INTO [project_members] ([project_id], [user_id], [role_in_project], [joined_at])
    VALUES (1, 4, 'visitor', '2026-02-02 08:30:00');
END
GO

-- โปรเจกต์ที่ 2: Campus Event Planner
-- User 2 เป็น host, User 1 เป็น member
IF NOT EXISTS (SELECT 1 FROM [project_members] WHERE [project_id] = 2 AND [user_id] = 2)
BEGIN
    INSERT INTO [project_members] ([project_id], [user_id], [role_in_project], [joined_at])
    VALUES (2, 2, 'host', '2026-02-05 13:30:00');
END
GO

IF NOT EXISTS (SELECT 1 FROM [project_members] WHERE [project_id] = 2 AND [user_id] = 1)
BEGIN
    INSERT INTO [project_members] ([project_id], [user_id], [role_in_project], [joined_at])
    VALUES (2, 1, 'member', '2026-02-05 14:00:00');
END
GO

-- ==============================================================================
-- [ส่วนที่ 3] ตรวจสอบข้อมูลสมาชิกในแต่ละโปรเจกต์
-- ==============================================================================
SELECT 
    pm.[id],
    p.[project_name],
    p.[subject_id],
    u.[display_name] AS member_name,
    u.[role] AS system_role,
    pm.[role_in_project],
    pm.[joined_at]
FROM [project_members] pm
JOIN [projects] p ON pm.[project_id] = p.[project_id]
JOIN [users] u ON pm.[user_id] = u.[user_id]
ORDER BY p.[project_id], pm.[joined_at];
GO
