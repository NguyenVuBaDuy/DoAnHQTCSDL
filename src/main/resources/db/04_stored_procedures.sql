

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
