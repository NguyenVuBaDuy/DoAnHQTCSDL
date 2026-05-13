-- ============================================================
-- FILE: fn_tinh_giam_gia_voucher.sql
-- DESC: Tính số tiền được giảm từ voucher
-- PARAMETERS:
--   IN  p_mavoucher  VARCHAR2  -- Mã voucher
--   IN  p_tongtien   NUMBER    -- Tổng tiền đơn hàng
-- RETURN: NUMBER  -- Số tiền được giảm
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE FUNCTION fn_tinh_giam_gia_voucher (
  p_mavoucher  IN VARCHAR2,
  p_tongtien   IN NUMBER
) RETURN NUMBER
AS
  v_giam_gia NUMBER := 0;
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Tính số tiền thực tế được giảm dựa trên loại voucher.
  -- LOGIC:
  --   1. Lấy thông tin voucher: LOAI, GIATRI, GIATRI_TOI_DA
  --   2. Nếu LOAI = 'PhanTram':
  --      v_giam_gia := p_tongtien * GIATRI / 100
  --      Nếu GIATRI_TOI_DA IS NOT NULL AND v_giam_gia > GIATRI_TOI_DA:
  --        v_giam_gia := GIATRI_TOI_DA
  --   3. Nếu LOAI = 'SoTienCoDinh':
  --      v_giam_gia := GIATRI
  --   4. Nếu v_giam_gia > p_tongtien:
  --      v_giam_gia := p_tongtien  (không giảm quá tổng tiền)
  --   5. RETURN v_giam_gia
  RETURN v_giam_gia;
END fn_tinh_giam_gia_voucher;
/

PROMPT === fn_tinh_giam_gia_voucher: Created ===
-- ============================================================
-- FILE: fn_kiem_tra_voucher_hop_le.sql
-- DESC: Kiểm tra voucher có hợp lệ để sử dụng hay không
-- PARAMETERS:
--   IN  p_mavoucher  VARCHAR2  -- Mã voucher cần kiểm tra
--   IN  p_tongtien   NUMBER    -- Tổng tiền đơn hàng
-- RETURN: NUMBER  -- 1 = hợp lệ, 0 = không hợp lệ
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE FUNCTION fn_kiem_tra_voucher_hop_le (
  p_mavoucher  IN VARCHAR2,
  p_tongtien   IN NUMBER
) RETURN NUMBER
AS
  v_result NUMBER := 0;
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Kiểm tra tất cả điều kiện để voucher có thể sử dụng.
  --   Trả về 1 nếu hợp lệ, 0 nếu không.
  -- CHECKS:
  --   1. Voucher tồn tại trong bảng VOUCHER
  --   2. TRANGTHAI = 'HoatDong'
  --   3. SYSTIMESTAMP BETWEEN NGAYBATDAU AND NGAYHETHAN
  --   4. Nếu SOLUONG IS NOT NULL: SOLUONG_DA_DUNG < SOLUONG
  --   5. p_tongtien >= DIEUKIEN_TOITHIEU
  --   Nếu tất cả đều đúng → v_result := 1
  RETURN v_result;
END fn_kiem_tra_voucher_hop_le;
/

PROMPT === fn_kiem_tra_voucher_hop_le: Created ===
-- ============================================================
-- FILE: fn_get_ton_kho.sql
-- DESC: Lấy số lượng tồn kho của biến thể tại cửa hàng
-- PARAMETERS:
--   IN  p_mach       VARCHAR2  -- Mã cửa hàng
--   IN  p_mabienthe  VARCHAR2  -- Mã biến thể
-- RETURN: NUMBER  -- Số lượng tồn kho (0 nếu không có)
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE FUNCTION fn_get_ton_kho (
  p_mach       IN VARCHAR2,
  p_mabienthe  IN VARCHAR2
) RETURN NUMBER
AS
  v_soluong NUMBER := 0;
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Trả về số lượng tồn kho của một biến thể sản phẩm
  --   tại một cửa hàng cụ thể.
  -- LOGIC:
  --   1. SELECT SOLUONG INTO v_soluong FROM TONKHO
  --      WHERE MACH = p_mach AND MABIENTHE = p_mabienthe
  --   2. Nếu NO_DATA_FOUND → v_soluong := 0
  --   3. RETURN v_soluong
  RETURN v_soluong;
END fn_get_ton_kho;
/

PROMPT === fn_get_ton_kho: Created ===
-- ============================================================
-- FILE: fn_doanh_thu_cuahang.sql
-- DESC: Tính tổng doanh thu cửa hàng trong khoảng thời gian
-- PARAMETERS:
--   IN  p_mach     VARCHAR2   -- Mã cửa hàng
--   IN  p_tungay   TIMESTAMP  -- Ngày bắt đầu
--   IN  p_denngay  TIMESTAMP  -- Ngày kết thúc
-- RETURN: NUMBER  -- Tổng doanh thu (0 nếu không có)
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE FUNCTION fn_doanh_thu_cuahang (
  p_mach     IN VARCHAR2,
  p_tungay   IN TIMESTAMP,
  p_denngay  IN TIMESTAMP
) RETURN NUMBER
AS
  v_doanh_thu NUMBER := 0;
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Tính tổng doanh thu (TONGTIEN - GIATRI_GIAM) của tất cả
  --   hóa đơn HoanThanh tại cửa hàng trong khoảng thời gian.
  -- LOGIC:
  --   1. SELECT NVL(SUM(TONGTIEN - GIATRI_GIAM), 0) INTO v_doanh_thu
  --      FROM HOADON
  --      WHERE MACH = p_mach
  --        AND TRANGTHAI = 'HoanThanh'
  --        AND NGAYLAP BETWEEN p_tungay AND p_denngay
  --   2. RETURN v_doanh_thu
  RETURN v_doanh_thu;
END fn_doanh_thu_cuahang;
/

PROMPT === fn_doanh_thu_cuahang: Created ===
