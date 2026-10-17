-- ==============================================================================
-- File: notification.sql
-- Description: โครงสร้างตาราง notifications และข้อมูลจำลอง (Sprint 1)
-- Target DBMS: Microsoft SQL Server 2022
-- ==============================================================================

USE [master];
GO

-- ==============================================================================
-- [ส่วนที่ 1] โครงสร้างตาราง (DDL Reference)
-- ==============================================================================
/*
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='notifications' AND xtype='U')
BEGIN
    CREATE TABLE [notifications] (
        [id] INT IDENTITY(1,1) PRIMARY KEY,
        [user_id] INT NOT NULL,                     -- ผู้รับแจ้งเตือน (FK -> users.user_id)
        [title] NVARCHAR(150) NOT NULL,             -- หัวข้อแจ้งเตือน
        [message] NVARCHAR(500) NOT NULL,           -- ข้อความแจ้งเตือน
        [type] VARCHAR(50) NOT NULL,                -- เช่น 'invite', 'task_assigned'
        [is_read] BIT NOT NULL DEFAULT 0,           -- 0 = false, 1 = true
        [created_at] DATETIME2 DEFAULT GETDATE(),
        CONSTRAINT FK_Notification_User FOREIGN KEY ([user_id]) REFERENCES [users]([user_id])
    );
END
GO
*/

-- ==============================================================================
-- [ส่วนที่ 2] ข้อมูลเริ่มต้นจำลอง (Mock / Seed Data)
-- ==============================================================================

IF NOT EXISTS (SELECT 1 FROM [notifications] WHERE [user_id] = 2 AND [title] = N'คุณได้รับมอบหมายงานใหม่')
BEGIN
    INSERT INTO [notifications] ([user_id], [title], [message], [type], [is_read], [created_at])
    VALUES (
        2,
        N'คุณได้รับมอบหมายงานใหม่',
        N'Somchai ได้มอบหมายงาน "พัฒนาระบบ Authentication และ JWT" ให้คุณ',
        'task_assigned',
        0,
        '2026-02-05 10:05:00'
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM [notifications] WHERE [user_id] = 3 AND [title] = N'คำเชิญเข้าร่วมกลุ่มโปรเจกต์')
BEGIN
    INSERT INTO [notifications] ([user_id], [title], [message], [type], [is_read], [created_at])
    VALUES (
        3,
        N'คำเชิญเข้าร่วมกลุ่มโปรเจกต์',
        N'คุณได้รับคำเชิญให้เข้าร่วมกลุ่มโปรเจกต์ "TU-WorkSpace-Group11"',
        'invite',
        1,
        '2026-02-01 10:30:00'
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM [notifications] WHERE [user_id] = 4 AND [title] = N'การเข้าตรวจเยี่ยมโปรเจกต์')
BEGIN
    INSERT INTO [notifications] ([user_id], [title], [message], [type], [is_read], [created_at])
    VALUES (
        4,
        N'การเข้าตรวจเยี่ยมโปรเจกต์',
        N'คุณได้รับสิทธิ์เข้าดู (visitor) ในโปรเจกต์ "TU-WorkSpace-Group11"',
        'invite',
        0,
        '2026-02-02 08:35:00'
    );
END
GO

-- ==============================================================================
-- [ส่วนที่ 3] ตรวจสอบข้อมูลการแจ้งเตือน
-- ==============================================================================
SELECT 
    n.[id],
    u.[display_name] AS notify_to,
    n.[title],
    n.[message],
    n.[type],
    CASE WHEN n.[is_read] = 1 THEN 'true (read)' ELSE 'false (unread)' END AS [is_read],
    n.[created_at]
FROM [notifications] n
JOIN [users] u ON n.[user_id] = u.[user_id]
ORDER BY n.[created_at] DESC;
GO
