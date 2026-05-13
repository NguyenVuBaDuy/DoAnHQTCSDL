-- ============================================================
-- FILE: reset_all.sql
-- DESC: Xóa toàn bộ đối tượng trong schema hiện tại
--       Bao gồm: tables, sequences, triggers, procedures, functions
-- WARNING: Script này sẽ XÓA TẤT CẢ dữ liệu!
-- ============================================================

-- Xóa tất cả triggers
BEGIN
  FOR t IN (SELECT trigger_name FROM user_triggers) LOOP
    EXECUTE IMMEDIATE 'DROP TRIGGER "' || t.trigger_name || '"';
  END LOOP;
END;
/

-- Xóa tất cả procedures
BEGIN
  FOR p IN (SELECT object_name FROM user_objects WHERE object_type = 'PROCEDURE') LOOP
    EXECUTE IMMEDIATE 'DROP PROCEDURE "' || p.object_name || '"';
  END LOOP;
END;
/

-- Xóa tất cả functions
BEGIN
  FOR f IN (SELECT object_name FROM user_objects WHERE object_type = 'FUNCTION') LOOP
    EXECUTE IMMEDIATE 'DROP FUNCTION "' || f.object_name || '"';
  END LOOP;
END;
/

-- Xóa tất cả tables (CASCADE CONSTRAINTS để xóa FK trước)
BEGIN
  FOR t IN (SELECT table_name FROM user_tables) LOOP
    EXECUTE IMMEDIATE 'DROP TABLE "' || t.table_name || '" CASCADE CONSTRAINTS';
  END LOOP;
END;
/

-- Xóa sequences do user tạo (SEQ_*), KHÔNG xóa identity sequences (ISEQ$$_*)
BEGIN
  FOR s IN (SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'SEQ_%') LOOP
    EXECUTE IMMEDIATE 'DROP SEQUENCE "' || s.sequence_name || '"';
  END LOOP;
END;
/
