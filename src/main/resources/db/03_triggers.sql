-- ============================================================
-- FILE: trg_update_tonkho_after_nhap.sql
-- DESC: Tự động tăng tồn kho khi thêm chi tiết phiếu nhập
-- TABLE: CHITIETPHIEUNHAP
-- EVENT: AFTER INSERT
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE TRIGGER trg_update_tonkho_after_nhap
  AFTER INSERT ON "CHITIETPHIEUNHAP"
  FOR EACH ROW
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Khi thêm dòng chi tiết phiếu nhập (CHITIETPHIEUNHAP), tự động
  --   tăng số lượng tồn kho tại cửa hàng tương ứng.
  -- STEPS:
  --   1. Lấy MACH từ PHIEUNHAP (qua :NEW.MAPN)
  --   2. MERGE INTO TONKHO: nếu đã có → cộng thêm SOLUONG,
  --      nếu chưa có → INSERT bản ghi mới với SOLUONG = :NEW.SOLUONG
  --   3. Cập nhật SERIALNUMBER.MACH nếu cần (gán serial vào cửa hàng)
  NULL;
END;
/

PROMPT === trg_update_tonkho_after_nhap: Created ===
-- ============================================================
-- FILE: trg_update_tonkho_after_ban.sql
-- DESC: Tự động giảm tồn kho khi thêm chi tiết hóa đơn (bán hàng)
-- TABLE: CHITIETHOADON
-- EVENT: AFTER INSERT
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE TRIGGER trg_update_tonkho_after_ban
  AFTER INSERT ON "CHITIETHOADON"
  FOR EACH ROW
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Khi thêm dòng chi tiết hóa đơn (CHITIETHOADON), tự động
  --   giảm tồn kho tại cửa hàng bán hàng (lấy từ HOADON.MACH).
  -- STEPS:
  --   1. Lấy MACH từ HOADON (qua :NEW.MAHD)
  --   2. Lấy MABIENTHE từ SERIALNUMBER (qua :NEW.MASN)
  --   3. UPDATE TONKHO: giảm SOLUONG đi 1 (mỗi serial = 1 sản phẩm)
  --   4. Kiểm tra SOLUONG >= 0, nếu < 0 thì RAISE_APPLICATION_ERROR
  NULL;
END;
/

PROMPT === trg_update_tonkho_after_ban: Created ===
-- ============================================================
-- FILE: trg_rollback_tonkho_after_huy_nhap.sql
-- DESC: Hoàn lại tồn kho khi hủy phiếu nhập
-- TABLE: PHIEUNHAP
-- EVENT: AFTER UPDATE (TRANGTHAI → 'HuyPhieu')
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE TRIGGER trg_rollback_tonkho_after_huy_nhap
  AFTER UPDATE OF "TRANGTHAI" ON "PHIEUNHAP"
  FOR EACH ROW
  WHEN (NEW."TRANGTHAI" = 'HuyPhieu' AND OLD."TRANGTHAI" != 'HuyPhieu')
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Khi phiếu nhập chuyển trạng thái sang 'HuyPhieu', tự động
  --   trừ lại số lượng tồn kho đã nhập trước đó.
  -- STEPS:
  --   1. Duyệt qua tất cả CHITIETPHIEUNHAP có MAPN = :NEW.MAPN
  --   2. Với mỗi chi tiết: UPDATE TONKHO SET SOLUONG = SOLUONG - ct.SOLUONG
  --      WHERE MACH = :NEW.MACH AND MABIENTHE = ct.MABIENTHE
  --   3. Cập nhật SERIALNUMBER liên quan: TRANGTHAI = 'HongHoc' hoặc xóa
  --   4. Kiểm tra SOLUONG >= 0, nếu < 0 thì RAISE_APPLICATION_ERROR
  NULL;
END;
/

PROMPT === trg_rollback_tonkho_after_huy_nhap: Created ===
-- ============================================================
-- FILE: trg_update_tonkho_after_chuyenkho.sql
-- DESC: Tự động cập nhật tồn kho khi chuyển kho (trừ nguồn, cộng đích)
-- TABLE: CHITIETPHIEUCHUYENKHO
-- EVENT: AFTER INSERT
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE TRIGGER trg_update_tonkho_after_chuyenkho
  AFTER INSERT ON "CHITIETPHIEUCHUYENKHO"
  FOR EACH ROW
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Khi thêm chi tiết phiếu chuyển kho, tự động:
  --   - Trừ tồn kho tại cửa hàng NGUỒN
  --   - Cộng tồn kho tại cửa hàng ĐÍCH
  -- STEPS:
  --   1. Lấy MACH_NGUON, MACH_DICH từ PHIEUCHUYENKHO (qua :NEW.MAPCK)
  --   2. UPDATE TONKHO SET SOLUONG = SOLUONG - :NEW.SOLUONG
  --      WHERE MACH = v_mach_nguon AND MABIENTHE = :NEW.MABIENTHE
  --   3. MERGE INTO TONKHO cho kho đích: nếu đã có → cộng thêm,
  --      nếu chưa có → INSERT mới
  --   4. Kiểm tra tồn kho nguồn >= 0
  --   5. Cập nhật SERIALNUMBER.MACH cho các serial được chuyển
  NULL;
END;
/

PROMPT === trg_update_tonkho_after_chuyenkho: Created ===
-- ============================================================
-- FILE: trg_serial_status_after_ban.sql
-- DESC: Tự động đổi trạng thái serial thành 'DaBan' khi bán hàng
-- TABLE: CHITIETHOADON
-- EVENT: AFTER INSERT
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE TRIGGER trg_serial_status_after_ban
  AFTER INSERT ON "CHITIETHOADON"
  FOR EACH ROW
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Khi thêm chi tiết hóa đơn, serial number tương ứng sẽ được
  --   đánh dấu là đã bán và không còn thuộc cửa hàng nào.
  -- STEPS:
  --   1. UPDATE SERIALNUMBER SET TRANGTHAI = 'DaBan', MACH = NULL
  --      WHERE MASN = :NEW.MASN
  --   2. Kiểm tra serial tồn tại và đang ở trạng thái 'TonKho'
  --      Nếu không → RAISE_APPLICATION_ERROR
  NULL;
END;
/

PROMPT === trg_serial_status_after_ban: Created ===
-- ============================================================
-- FILE: trg_serial_rollback_after_huy_hoadon.sql
-- DESC: Hoàn lại trạng thái serial khi hủy hóa đơn
-- TABLE: HOADON
-- EVENT: AFTER UPDATE (TRANGTHAI → 'DaHuy')
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE TRIGGER trg_serial_rollback_after_huy_hoadon
  AFTER UPDATE OF "TRANGTHAI" ON "HOADON"
  FOR EACH ROW
  WHEN (NEW."TRANGTHAI" = 'DaHuy' AND OLD."TRANGTHAI" != 'DaHuy')
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Khi hóa đơn chuyển trạng thái sang 'DaHuy', tất cả serial
  --   number trong hóa đơn đó sẽ được hoàn lại trạng thái 'TonKho'
  --   và gán lại vào cửa hàng ban đầu.
  -- STEPS:
  --   1. Duyệt tất cả CHITIETHOADON có MAHD = :NEW.MAHD
  --   2. Với mỗi MASN: UPDATE SERIALNUMBER SET
  --      TRANGTHAI = 'TonKho', MACH = :NEW.MACH
  --   3. Cộng lại TONKHO.SOLUONG cho mỗi biến thể tương ứng
  --   4. Nếu HOADON có MAVOUCHER → giảm VOUCHER.SOLUONG_DA_DUNG đi 1
  NULL;
END;
/

PROMPT === trg_serial_rollback_after_huy_hoadon: Created ===
-- ============================================================
-- FILE: trg_voucher_increment_after_hoadon.sql
-- DESC: Tự động tăng số lượng đã dùng của voucher khi tạo hóa đơn
-- TABLE: HOADON
-- EVENT: AFTER INSERT
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE TRIGGER trg_voucher_increment_after_hoadon
  AFTER INSERT ON "HOADON"
  FOR EACH ROW
  WHEN (NEW."MAVOUCHER" IS NOT NULL)
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Khi tạo hóa đơn có sử dụng voucher, tự động tăng
  --   SOLUONG_DA_DUNG của voucher đó lên 1.
  -- STEPS:
  --   1. UPDATE VOUCHER SET SOLUONG_DA_DUNG = SOLUONG_DA_DUNG + 1
  --      WHERE MAVOUCHER = :NEW.MAVOUCHER
  --   2. Kiểm tra nếu SOLUONG IS NOT NULL AND SOLUONG_DA_DUNG > SOLUONG
  --      → RAISE_APPLICATION_ERROR (voucher đã hết lượt sử dụng)
  NULL;
END;
/

PROMPT === trg_voucher_increment_after_hoadon: Created ===
