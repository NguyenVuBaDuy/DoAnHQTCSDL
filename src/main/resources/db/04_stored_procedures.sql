-- ============================================================
-- FILE: sp_tao_phieu_nhap.sql
-- DESC: Tạo phiếu nhập kho mới
-- PARAMETERS:
--   IN  p_mach    VARCHAR2  -- Mã cửa hàng nhận hàng
--   IN  p_mancc   VARCHAR2  -- Mã nhà cung cấp
--   IN  p_manv    VARCHAR2  -- Mã nhân viên tạo phiếu
--   IN  p_ghichu  VARCHAR2  -- Ghi chú (có thể NULL)
--   OUT p_mapn    VARCHAR2  -- Mã phiếu nhập được sinh ra
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE PROCEDURE sp_tao_phieu_nhap (
  p_mach    IN  VARCHAR2,
  p_mancc   IN  VARCHAR2,
  p_manv    IN  VARCHAR2,
  p_ghichu  IN  VARCHAR2 DEFAULT NULL,
  p_mapn    OUT VARCHAR2
) AS
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Tạo phiếu nhập kho mới với trạng thái 'NhapKho'.
  --   Mã phiếu nhập được sinh tự động từ SEQ_PHIEUNHAP.
  -- STEPS:
  --   1. Validate: kiểm tra p_mach tồn tại trong CUAHANG và đang HoatDong
  --   2. Validate: kiểm tra p_mancc tồn tại trong NHACUNGCAP và đang HoatDong
  --   3. Validate: kiểm tra p_manv tồn tại trong NHANVIEN
  --   4. Sinh mã: p_mapn := 'PN' || LPAD(SEQ_PHIEUNHAP.NEXTVAL, 6, '0')
  --   5. INSERT INTO PHIEUNHAP với NGAYNHAP = SYSTIMESTAMP, TRANGTHAI = 'NhapKho'
  --   6. COMMIT
  -- EXCEPTION:
  --   Nếu lỗi → ROLLBACK và RAISE
  NULL;
END sp_tao_phieu_nhap;
/

PROMPT === sp_tao_phieu_nhap: Created ===
-- ============================================================
-- FILE: sp_huy_phieu_nhap.sql
-- DESC: Hủy phiếu nhập kho (chuyển trạng thái → HuyPhieu)
-- PARAMETERS:
--   IN  p_mapn  VARCHAR2  -- Mã phiếu nhập cần hủy
--   IN  p_manv  VARCHAR2  -- Mã nhân viên thực hiện hủy
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE PROCEDURE sp_huy_phieu_nhap (
  p_mapn  IN VARCHAR2,
  p_manv  IN VARCHAR2
) AS
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Hủy phiếu nhập kho. Chỉ hủy được phiếu đang ở trạng thái 'NhapKho'.
  --   Khi hủy, trigger trg_rollback_tonkho_after_huy_nhap sẽ tự động
  --   hoàn lại tồn kho.
  -- STEPS:
  --   1. Validate: kiểm tra p_mapn tồn tại trong PHIEUNHAP
  --   2. Validate: TRANGTHAI hiện tại phải là 'NhapKho'
  --      Nếu đã là 'HuyPhieu' → RAISE lỗi 'Phiếu đã bị hủy trước đó'
  --   3. UPDATE PHIEUNHAP SET TRANGTHAI = 'HuyPhieu'
  --      WHERE MAPN = p_mapn
  --      (Trigger sẽ tự động xử lý hoàn kho)
  --   4. COMMIT
  -- EXCEPTION:
  --   Nếu lỗi → ROLLBACK và RAISE
  NULL;
END sp_huy_phieu_nhap;
/

PROMPT === sp_huy_phieu_nhap: Created ===
-- ============================================================
-- FILE: sp_duyet_chuyen_kho.sql
-- DESC: Duyệt phiếu chuyển kho (chuyển trạng thái → DaChuyenKho)
-- PARAMETERS:
--   IN  p_mapck  VARCHAR2  -- Mã phiếu chuyển kho cần duyệt
--   IN  p_manv   VARCHAR2  -- Mã nhân viên duyệt phiếu
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE PROCEDURE sp_duyet_chuyen_kho (
  p_mapck  IN VARCHAR2,
  p_manv   IN VARCHAR2
) AS
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Duyệt phiếu chuyển kho từ trạng thái 'ChoDuyet' sang 'DaChuyenKho'.
  --   Chỉ duyệt được phiếu đang ở trạng thái 'ChoDuyet'.
  --   Khi duyệt, trigger sẽ tự động trừ kho nguồn và cộng kho đích.
  -- STEPS:
  --   1. Validate: kiểm tra p_mapck tồn tại trong PHIEUCHUYENKHO
  --   2. Validate: TRANGTHAI hiện tại phải là 'ChoDuyet'
  --   3. Validate: kiểm tra tồn kho nguồn đủ cho tất cả chi tiết
  --      - Duyệt CHITIETPHIEUCHUYENKHO WHERE MAPCK = p_mapck
  --      - So sánh SOLUONG với TONKHO tại MACH_NGUON
  --   4. UPDATE PHIEUCHUYENKHO SET TRANGTHAI = 'DaChuyenKho'
  --   5. Cập nhật SERIALNUMBER.MACH cho các serial được chuyển
  --   6. COMMIT
  -- EXCEPTION:
  --   Nếu tồn kho không đủ → RAISE lỗi kèm chi tiết biến thể thiếu
  --   Nếu lỗi khác → ROLLBACK và RAISE
  NULL;
END sp_duyet_chuyen_kho;
/

PROMPT === sp_duyet_chuyen_kho: Created ===
-- ============================================================
-- FILE: sp_tao_hoa_don.sql
-- DESC: Tạo hóa đơn bán hàng mới (tại quầy hoặc online)
-- PARAMETERS:
--   IN  p_mach      VARCHAR2  -- Mã cửa hàng bán
--   IN  p_manv      VARCHAR2  -- Mã nhân viên bán
--   IN  p_makh      VARCHAR2  -- Mã khách hàng (có thể NULL nếu khách lẻ)
--   IN  p_mavoucher VARCHAR2  -- Mã voucher (có thể NULL)
--   IN  p_pttt      VARCHAR2  -- Phương thức thanh toán
--   IN  p_loaihd    VARCHAR2  -- Loại hóa đơn: TaiQuay | Online
--   OUT p_mahd      VARCHAR2  -- Mã hóa đơn được sinh ra
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE PROCEDURE sp_tao_hoa_don (
  p_mach      IN  VARCHAR2,
  p_manv      IN  VARCHAR2,
  p_makh      IN  VARCHAR2 DEFAULT NULL,
  p_mavoucher IN  VARCHAR2 DEFAULT NULL,
  p_pttt      IN  VARCHAR2,
  p_loaihd    IN  VARCHAR2,
  p_mahd      OUT VARCHAR2
) AS
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Tạo hóa đơn mới. Nếu có voucher, kiểm tra tính hợp lệ
  --   và tính giá trị giảm giá trước khi tạo.
  -- STEPS:
  --   1. Validate: kiểm tra p_mach tồn tại và đang HoatDong
  --   2. Validate: kiểm tra p_manv thuộc cửa hàng p_mach
  --   3. Nếu p_makh IS NOT NULL: kiểm tra khách hàng tồn tại
  --   4. Nếu p_mavoucher IS NOT NULL:
  --      a. Gọi fn_kiem_tra_voucher_hop_le(p_mavoucher, 0) = 1
  --      b. Tính GIATRI_GIAM = fn_tinh_giam_gia_voucher(p_mavoucher, TONGTIEN)
  --   5. Sinh mã: p_mahd := 'HD' || LPAD(SEQ_HOADON.NEXTVAL, 6, '0')
  --   6. INSERT INTO HOADON với NGAYLAP = SYSTIMESTAMP
  --      TRANGTHAI = 'ChoDuyet' (online) hoặc 'HoanThanh' (tại quầy)
  --   7. COMMIT
  -- EXCEPTION:
  --   Nếu voucher không hợp lệ → RAISE lỗi
  --   Nếu lỗi khác → ROLLBACK và RAISE
  NULL;
END sp_tao_hoa_don;
/

PROMPT === sp_tao_hoa_don: Created ===
-- ============================================================
-- FILE: sp_huy_hoa_don.sql
-- DESC: Hủy hóa đơn (chuyển trạng thái → DaHuy)
-- PARAMETERS:
--   IN  p_mahd   VARCHAR2  -- Mã hóa đơn cần hủy
--   IN  p_manv   VARCHAR2  -- Mã nhân viên thực hiện hủy
--   IN  p_lydo   VARCHAR2  -- Lý do hủy đơn
-- AUTHOR: [TODO]
-- ============================================================

CREATE OR REPLACE PROCEDURE sp_huy_hoa_don (
  p_mahd   IN VARCHAR2,
  p_manv   IN VARCHAR2,
  p_lydo   IN VARCHAR2 DEFAULT NULL
) AS
BEGIN
  -- TODO: Implement
  -- BUSINESS RULE:
  --   Hủy hóa đơn. Chỉ hủy được hóa đơn ở trạng thái:
  --   'ChoDuyet', 'DaXacNhan'. KHÔNG hủy được 'DangGiao' hoặc 'HoanThanh'.
  --   Khi hủy, trigger sẽ tự động hoàn serial và tồn kho.
  -- STEPS:
  --   1. Validate: kiểm tra p_mahd tồn tại trong HOADON
  --   2. Validate: TRANGTHAI phải là 'ChoDuyet' hoặc 'DaXacNhan'
  --      Nếu là 'DangGiao' → RAISE lỗi 'Không thể hủy đơn đang giao'
  --      Nếu là 'HoanThanh' → RAISE lỗi 'Đơn đã hoàn thành, không thể hủy'
  --      Nếu là 'DaHuy' → RAISE lỗi 'Đơn đã bị hủy trước đó'
  --   3. UPDATE HOADON SET TRANGTHAI = 'DaHuy'
  --      (Trigger trg_serial_rollback_after_huy_hoadon sẽ hoàn serial)
  --   4. Nếu có VANCHUYEN → UPDATE TRANGTHAIVC = 'HoanHang'
  --   5. COMMIT
  -- EXCEPTION:
  --   Nếu lỗi → ROLLBACK và RAISE
  NULL;
END sp_huy_hoa_don;
/

PROMPT === sp_huy_hoa_don: Created ===
