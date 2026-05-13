-- ============================================================
-- FILE: TRG_NHANVIEN_MANV
-- DESC: Tự sinh mã nhân viên MANV = YYYY + số thứ tự 4 chữ số
-- TABLE: NHANVIEN
-- EVENT: BEFORE INSERT (khi MANV IS NULL)
-- ============================================================

CREATE OR REPLACE TRIGGER "TRG_NHANVIEN_MANV"
  BEFORE INSERT ON "NHANVIEN"
  FOR EACH ROW
  WHEN (NEW."MANV" IS NULL)
BEGIN
  :NEW."MANV" := TO_CHAR(SYSDATE, 'YYYY') || LPAD("SEQ_NV_SO".NEXTVAL, 4, '0');
END;
/

PROMPT === TRG_NHANVIEN_MANV: Created ===
