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

-- ==========================================
-- 1. THÊM CỬA HÀNG
-- ==========================================
CREATE OR REPLACE PROCEDURE INSERT_CUAHANG (
    p_TENCH           IN VARCHAR2,
    p_DIACHI          IN VARCHAR2,
    p_SDT             IN VARCHAR2,
    p_EMAIL           IN VARCHAR2,
    p_NGAYKHAITRUONG  IN DATE,
    p_TRANGTHAI       IN VARCHAR2
)
AS
    v_count NUMBER;
BEGIN
    -- Kiểm tra tên cửa hàng không được trống
    IF p_TENCH IS NULL OR TRIM(p_TENCH) IS NULL THEN
        RAISE_APPLICATION_ERROR(-20001, 'Ten cua hang khong duoc de trong');
    END IF;

    -- Kiểm tra trạng thái hợp lệ
    IF p_TRANGTHAI NOT IN ('HoatDong', 'DongCua', 'TamNgung') THEN
        RAISE_APPLICATION_ERROR(-20002, 'Trang thai khong hop le. Chi chap nhan: HoatDong, DongCua, TamNgung');
    END IF;

    -- Kiểm tra số điện thoại đã tồn tại chưa
    IF p_SDT IS NOT NULL THEN
        SELECT COUNT(*) INTO v_count FROM CUAHANG WHERE SDT = p_SDT;
        IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20003, 'So dien thoai da ton tai');
        END IF;
    END IF;

    -- Kiểm tra email đã tồn tại chưa
    IF p_EMAIL IS NOT NULL THEN
        SELECT COUNT(*) INTO v_count FROM CUAHANG WHERE EMAIL = p_EMAIL;
        IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20004, 'Email da ton tai');
        END IF;
    END IF;

    -- Insert dữ liệu
    INSERT INTO CUAHANG (TENCH, DIACHI, SDT, EMAIL, NGAYKHAITRUONG, TRANGTHAI)
    VALUES (p_TENCH, p_DIACHI, p_SDT, p_EMAIL, p_NGAYKHAITRUONG, p_TRANGTHAI);

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Them cua hang thanh cong');

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END INSERT_CUAHANG;
/

PROMPT === INSERT_CUAHANG: Created ===

-- ==========================================
-- 2. CẬP NHẬT CỬA HÀNG
-- ==========================================
CREATE OR REPLACE PROCEDURE UPDATE_CUAHANG (
    p_MACH            IN NUMBER,
    p_TENCH           IN VARCHAR2,
    p_DIACHI          IN VARCHAR2,
    p_SDT             IN VARCHAR2,
    p_EMAIL           IN VARCHAR2,
    p_NGAYKHAITRUONG  IN DATE,
    p_TRANGTHAI       IN VARCHAR2
)
AS
    v_count NUMBER;
BEGIN
    -- Kiểm tra cửa hàng tồn tại
    SELECT COUNT(*) INTO v_count FROM CUAHANG WHERE MACH = p_MACH;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20010, 'Cua hang khong ton tai');
    END IF;

    -- Kiểm tra tên cửa hàng không được trống
    IF p_TENCH IS NULL OR TRIM(p_TENCH) IS NULL THEN
        RAISE_APPLICATION_ERROR(-20001, 'Ten cua hang khong duoc de trong');
    END IF;

    -- Kiểm tra trạng thái hợp lệ
    IF p_TRANGTHAI NOT IN ('HoatDong', 'DongCua', 'TamNgung') THEN
        RAISE_APPLICATION_ERROR(-20002, 'Trang thai khong hop le. Chi chap nhan: HoatDong, DongCua, TamNgung');
    END IF;

    -- Kiểm tra số điện thoại trùng với cửa hàng khác
    IF p_SDT IS NOT NULL THEN
        SELECT COUNT(*) INTO v_count FROM CUAHANG WHERE SDT = p_SDT AND MACH != p_MACH;
        IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20003, 'So dien thoai da ton tai');
        END IF;
    END IF;

    -- Kiểm tra email trùng với cửa hàng khác
    IF p_EMAIL IS NOT NULL THEN
        SELECT COUNT(*) INTO v_count FROM CUAHANG WHERE EMAIL = p_EMAIL AND MACH != p_MACH;
        IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20004, 'Email da ton tai');
        END IF;
    END IF;

    -- Cập nhật dữ liệu
    UPDATE CUAHANG
    SET TENCH          = p_TENCH,
        DIACHI         = p_DIACHI,
        SDT            = p_SDT,
        EMAIL          = p_EMAIL,
        NGAYKHAITRUONG = p_NGAYKHAITRUONG,
        TRANGTHAI      = p_TRANGTHAI
    WHERE MACH = p_MACH;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Cap nhat cua hang thanh cong');

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END UPDATE_CUAHANG;
/

PROMPT === UPDATE_CUAHANG: Created ===

-- ==========================================
-- 3. XÓA CỬA HÀNG
-- ==========================================
CREATE OR REPLACE PROCEDURE DELETE_CUAHANG (
    p_MACH IN NUMBER
)
AS
    v_count NUMBER;
BEGIN
    -- Kiểm tra cửa hàng tồn tại
    SELECT COUNT(*) INTO v_count FROM CUAHANG WHERE MACH = p_MACH;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20010, 'Cua hang khong ton tai');
    END IF;

    -- Kiểm tra có nhân viên thuộc cửa hàng không
    SELECT COUNT(*) INTO v_count FROM NHANVIEN WHERE MACH = p_MACH;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20011, 'Khong the xoa cua hang vi con nhan vien thuoc cua hang nay');
    END IF;

    -- Kiểm tra có tồn kho thuộc cửa hàng không
    SELECT COUNT(*) INTO v_count FROM TONKHO WHERE MACH = p_MACH;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20012, 'Khong the xoa cua hang vi con ton kho thuoc cua hang nay');
    END IF;

    -- Xóa cửa hàng
    DELETE FROM CUAHANG WHERE MACH = p_MACH;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Xoa cua hang thanh cong');

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END DELETE_CUAHANG;
/

PROMPT === DELETE_CUAHANG: Created ===

-- ==========================================
-- 4. THAY ĐỔI TRẠNG THÁI CỬA HÀNG
-- ==========================================
CREATE OR REPLACE PROCEDURE CHANGE_STATUS_CUAHANG (
    p_MACH      IN NUMBER,
    p_TRANGTHAI IN VARCHAR2
)
AS
    v_count NUMBER;
BEGIN
    -- Kiểm tra cửa hàng tồn tại
    SELECT COUNT(*) INTO v_count FROM CUAHANG WHERE MACH = p_MACH;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20010, 'Cua hang khong ton tai');
    END IF;

    -- Kiểm tra trạng thái hợp lệ
    IF p_TRANGTHAI NOT IN ('HoatDong', 'DongCua', 'TamNgung') THEN
        RAISE_APPLICATION_ERROR(-20002, 'Trang thai khong hop le. Chi chap nhan: HoatDong, DongCua, TamNgung');
    END IF;

    -- Cập nhật trạng thái
    UPDATE CUAHANG
    SET TRANGTHAI = p_TRANGTHAI
    WHERE MACH = p_MACH;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Thay doi trang thai thanh cong');

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END CHANGE_STATUS_CUAHANG;
/

PROMPT === CHANGE_STATUS_CUAHANG: Created ===

-- ==========================================
-- 5. THÊM NHÂN VIÊN (TẠO CẢ TÀI KHOẢN)
-- ==========================================
CREATE OR REPLACE PROCEDURE INSERT_NHANVIEN (
    p_MACH      IN NUMBER,
    p_CCCD      IN VARCHAR2,
    p_HOTEN     IN VARCHAR2,
    p_NGAYSINH  IN DATE,
    p_GIOITINH  IN VARCHAR2,
    p_SDT       IN VARCHAR2,
    p_DIACHI    IN VARCHAR2,
    p_CHUCVU    IN VARCHAR2,
    p_MANHOM    IN NUMBER,
    p_PASSWORD  IN VARCHAR2,
    p_TRANGTHAI IN VARCHAR2
)
AS
    v_manv VARCHAR2(20);
    v_count NUMBER;
BEGIN
    -- Kiểm tra họ tên
    IF p_HOTEN IS NULL OR TRIM(p_HOTEN) IS NULL THEN
        RAISE_APPLICATION_ERROR(-20021, 'Ho ten khong duoc de trong');
    END IF;

    -- Kiểm tra CCCD
    IF p_CCCD IS NOT NULL THEN
        SELECT COUNT(*) INTO v_count FROM NHANVIEN WHERE CCCD = p_CCCD;
        IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20022, 'CCCD da ton tai');
        END IF;
    END IF;

    -- Kiểm tra Nhóm
    SELECT COUNT(*) INTO v_count FROM NHOM WHERE MANHOM = p_MANHOM;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20023, 'Nhom quyen khong ton tai');
    END IF;

    -- Nếu có MACH thì kiểm tra cửa hàng
    IF p_MACH IS NOT NULL THEN
        SELECT COUNT(*) INTO v_count FROM CUAHANG WHERE MACH = p_MACH;
        IF v_count = 0 THEN
            RAISE_APPLICATION_ERROR(-20010, 'Cua hang khong ton tai');
        END IF;
    END IF;

    -- Insert NHANVIEN và lấy MANV
    INSERT INTO NHANVIEN (MACH, CCCD, HOTEN, NGAYSINH, GIOITINH, SDT, DIACHI, CHUCVU)
    VALUES (p_MACH, p_CCCD, p_HOTEN, p_NGAYSINH, p_GIOITINH, p_SDT, p_DIACHI, p_CHUCVU)
    RETURNING MANV INTO v_manv;

    -- Insert TAIKHOAN
    INSERT INTO TAIKHOAN (MANHOM, MANV, PASSWORD, TRANGTHAI)
    VALUES (p_MANHOM, v_manv, p_PASSWORD, NVL(p_TRANGTHAI, 'HoatDong'));

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Them nhan vien thanh cong: ' || v_manv);
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END INSERT_NHANVIEN;
/

PROMPT === INSERT_NHANVIEN: Created ===

-- ==========================================
-- 6. CẬP NHẬT NHÂN VIÊN
-- ==========================================
CREATE OR REPLACE PROCEDURE UPDATE_NHANVIEN (
    p_MANV      IN VARCHAR2,
    p_MACH      IN NUMBER,
    p_CCCD      IN VARCHAR2,
    p_HOTEN     IN VARCHAR2,
    p_NGAYSINH  IN DATE,
    p_GIOITINH  IN VARCHAR2,
    p_SDT       IN VARCHAR2,
    p_DIACHI    IN VARCHAR2,
    p_CHUCVU    IN VARCHAR2
)
AS
    v_count NUMBER;
BEGIN
    -- Kiểm tra NV tồn tại
    SELECT COUNT(*) INTO v_count FROM NHANVIEN WHERE MANV = p_MANV;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20020, 'Nhan vien khong ton tai');
    END IF;

    -- Kiểm tra họ tên
    IF p_HOTEN IS NULL OR TRIM(p_HOTEN) IS NULL THEN
        RAISE_APPLICATION_ERROR(-20021, 'Ho ten khong duoc de trong');
    END IF;

    -- Kiểm tra CCCD trùng
    IF p_CCCD IS NOT NULL THEN
        SELECT COUNT(*) INTO v_count FROM NHANVIEN WHERE CCCD = p_CCCD AND MANV != p_MANV;
        IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20022, 'CCCD da ton tai');
        END IF;
    END IF;

    -- Update NHANVIEN
    UPDATE NHANVIEN
    SET MACH = p_MACH,
        CCCD = p_CCCD,
        HOTEN = p_HOTEN,
        NGAYSINH = p_NGAYSINH,
        GIOITINH = p_GIOITINH,
        SDT = p_SDT,
        DIACHI = p_DIACHI,
        CHUCVU = p_CHUCVU
    WHERE MANV = p_MANV;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Cap nhat nhan vien thanh cong');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END UPDATE_NHANVIEN;
/

PROMPT === UPDATE_NHANVIEN: Created ===

-- ==========================================
-- 7. VÔ HIỆU HÓA NHÂN VIÊN
-- ==========================================
CREATE OR REPLACE PROCEDURE DISABLE_NHANVIEN (
    p_MANV IN VARCHAR2
)
AS
    v_count NUMBER;
BEGIN
    -- Kiểm tra TK tồn tại
    SELECT COUNT(*) INTO v_count FROM TAIKHOAN WHERE MANV = p_MANV;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20020, 'Nhan vien khong ton tai');
    END IF;

    -- Update TRANGTHAI TAIKHOAN = KhoaCung
    UPDATE TAIKHOAN
    SET TRANGTHAI = 'KhoaCung'
    WHERE MANV = p_MANV;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Vo hieu hoa nhan vien thanh cong');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END DISABLE_NHANVIEN;
/

PROMPT === DISABLE_NHANVIEN: Created ===
