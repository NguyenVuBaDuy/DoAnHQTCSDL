-- ============================================================
-- FILE: 02_sequences.sql
-- DESC: Tạo Oracle Sequences bổ sung (ngoài identity columns)
-- NOTE: Hầu hết bảng đã dùng GENERATED ALWAYS AS IDENTITY,
--       file này chỉ tạo sequence cho các trường hợp đặc biệt.
--       SEQ_NV_SO: dùng cho trigger TRG_NHANVIEN_MANV
--                  (đã tạo trong 00_create_tables.sql, chỉ tạo lại nếu chưa có)
-- ============================================================

-- Tạo SEQ_NV_SO nếu chưa tồn tại (đã được tạo trong 00_create_tables.sql)
-- Không cần tạo lại vì 00_create_tables.sql đã tạo rồi.
-- File này giữ lại để mở rộng thêm sequences khác trong tương lai.

PROMPT === 02_sequences.sql: Sequences hoan tat ===
