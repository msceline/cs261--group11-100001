-- ==============================================================================
-- File: task.sql
-- Description: โครงสร้างตาราง tasks และข้อมูลจำลอง (Sprint 1)
-- Target DBMS: Microsoft SQL Server 2022
-- ==============================================================================

USE [master];
GO

-- ==============================================================================
-- [ส่วนที่ 1] โครงสร้างตาราง (DDL Reference)
-- ==============================================================================
/*
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='tasks' AND xtype='U')
BEGIN
    CREATE TABLE [tasks] (
        [task_id] INT IDENTITY(1,1) PRIMARY KEY,
        [project_id] INT NOT NULL,
        [title] NVARCHAR(200) NOT NULL,
        [assignee_id] INT NULL,                     -- ว่างได้ถ้ายังไม่มอบหมาย
        [status] VARCHAR(20) NOT NULL DEFAULT 'todo', -- 'todo', 'doing', 'done'
        [due_date] DATETIME2 NULL,
        [task_description] NVARCHAR(MAX) NULL,
        [created_at] DATETIME2 DEFAULT GETDATE(),
        CONSTRAINT FK_Task_Project FOREIGN KEY ([project_id]) REFERENCES [projects]([project_id]),
        CONSTRAINT FK_Task_Assignee FOREIGN KEY ([assignee_id]) REFERENCES [users]([user_id])
    );
END
GO
*/

-- ==============================================================================
-- [ส่วนที่ 2] ข้อมูลเริ่มต้นจำลอง (Mock / Seed Data)
-- ==============================================================================

IF NOT EXISTS (SELECT 1 FROM [tasks] WHERE [title] = N'ออกแบบ ER Diagram และ Schema')
BEGIN
    INSERT INTO [tasks] ([project_id], [title], [assignee_id], [status], [due_date], [task_description], [created_at])
    VALUES (
        1,
        N'ออกแบบ ER Diagram และ Schema',
        1,
        'done',
        '2026-02-15 23:59:00',
        N'กำหนดตาราง users, projects, project_members, tasks, notifications',
        '2026-02-02 09:00:00'
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM [tasks] WHERE [title] = N'พัฒนาระบบ Authentication และ JWT')
BEGIN
    INSERT INTO [tasks] ([project_id], [title], [assignee_id], [status], [due_date], [task_description], [created_at])
    VALUES (
        1,
        N'พัฒนาระบบ Authentication และ JWT',
        2,
        'doing',
        '2026-02-20 23:59:00',
        N'เชื่อมต่อระบบ Login และสร้าง Token สำหรับตรวจสอบสิทธิ์',
        '2026-02-05 10:00:00'
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM [tasks] WHERE [title] = N'สร้างหน้า Dashboard แสดงรายการงาน')
BEGIN
    INSERT INTO [tasks] ([project_id], [title], [assignee_id], [status], [due_date], [task_description], [created_at])
    VALUES (
        1,
        N'สร้างหน้า Dashboard แสดงรายการงาน',
        3,
        'todo',
        '2026-02-28 23:59:00',
        N'ทำหน้า UI สำหรับดู Kanban Board ของแต่ละโปรเจกต์',
        '2026-02-06 11:30:00'
    );
END
GO

-- งานที่ยังไม่ได้มอบหมาย (assignee_id เป็น NULL)
IF NOT EXISTS (SELECT 1 FROM [tasks] WHERE [title] = N'เตรียมสไลด์นำเสนอ Sprint 1')
BEGIN
    INSERT INTO [tasks] ([project_id], [title], [assignee_id], [status], [due_date], [task_description], [created_at])
    VALUES (
        1,
        N'เตรียมสไลด์นำเสนอ Sprint 1',
        NULL,
        'todo',
        '2026-03-01 12:00:00',
        N'สรุปภาพรวมความคืบหน้าของ Sprint 1 เพื่อนำเสนออาจารย์',
        '2026-02-07 14:00:00'
    );
END
GO

-- ==============================================================================
-- [ส่วนที่ 3] ตรวจสอบข้อมูลงานในระบบ
-- ==============================================================================
SELECT 
    t.[task_id],
    p.[project_name],
    t.[title],
    COALESCE(u.[display_name], N'(ยังไม่ได้มอบหมาย)') AS assignee_name,
    t.[status],
    t.[due_date],
    t.[task_description],
    t.[created_at]
FROM [tasks] t
JOIN [projects] p ON t.[project_id] = p.[project_id]
LEFT JOIN [users] u ON t.[assignee_id] = u.[user_id];
GO
