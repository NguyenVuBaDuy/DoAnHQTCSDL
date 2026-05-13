-- ============================================================
-- FILE: 00_create_tables.sql
-- DESC: Tạo toàn bộ bảng cho hệ thống quản lý chuỗi cửa hàng
-- NOTE: Chạy file này TRƯỚC TIÊN
-- DB:   Oracle 21c XE
--
-- Quy ước kiểu PK:
--   - Hầu hết PK: NUMBER GENERATED ALWAYS AS IDENTITY (auto increment)
--   - MANV       : VARCHAR2(20), format YYYYxxxx (vd: 20250001)
--                  Sinh tự động qua trigger + sequence SEQ_NV_SO
--   - MAVOUCHER  : VARCHAR2(50), mã nhập tay (vd: SALE11, BIRTHDAY2024)
-- ============================================================

-- Xóa bảng nếu đã tồn tại (theo thứ tự reverse FK)
BEGIN
  FOR t IN (SELECT table_name FROM user_tables) LOOP
    EXECUTE IMMEDIATE 'DROP TABLE "' || t.table_name || '" CASCADE CONSTRAINTS';
  END LOOP;
END;
/

-- Xóa sequence do user tạo (SEQ_*), KHÔNG xóa identity sequences (ISEQ$$_*)
BEGIN
  FOR s IN (SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'SEQ_%') LOOP
    EXECUTE IMMEDIATE 'DROP SEQUENCE "' || s.sequence_name || '"';
  END LOOP;
END;
/

-- =========================
-- 1. NHOM (Nhóm quyền)
-- =========================
CREATE TABLE "NHOM" (
  "MANHOM"    NUMBER        GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "TENNHOM"   VARCHAR2(50)  NOT NULL
);

COMMENT ON COLUMN "NHOM"."TENNHOM" IS 'Admin | QuanLyCuaHang | NhanVienBan | Kho';

-- =========================
-- 2. TAIKHOAN
-- =========================
CREATE TABLE "TAIKHOAN" (
  "MATK"       NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "MANHOM"     NUMBER,
  "USERNAME"   VARCHAR2(50)   NOT NULL,
  "PASSWORD"   VARCHAR2(255)  NOT NULL,
  "TRANGTHAI"  VARCHAR2(20),
  CONSTRAINT "UQ_TAIKHOAN_USERNAME" UNIQUE ("USERNAME")
);

COMMENT ON COLUMN "TAIKHOAN"."TRANGTHAI" IS 'HoatDong | KhoaCung | KhoaTam';

-- =========================
-- 3. CUAHANG
-- =========================
CREATE TABLE "CUAHANG" (
  "MACH"            NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "TENCH"           VARCHAR2(100)  NOT NULL,
  "DIACHI"          VARCHAR2(255),
  "SDT"             VARCHAR2(15),
  "EMAIL"           VARCHAR2(100),
  "NGAYKHAITRUONG"  DATE,
  "TRANGTHAI"       VARCHAR2(20)
);

COMMENT ON COLUMN "CUAHANG"."TRANGTHAI" IS 'HoatDong | DongCua | TamNgung';

-- =========================
-- 4. NHANVIEN
-- PK: VARCHAR2(20) — format YYYYxxxx, sinh qua trigger + sequence
--
-- Sequence SEQ_NV_SO KHÔNG tự reset theo năm.
-- Nếu muốn reset đầu năm mới, DBA chạy thủ công:
--   DROP SEQUENCE "SEQ_NV_SO";
--   CREATE SEQUENCE "SEQ_NV_SO" START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
-- =========================
CREATE SEQUENCE "SEQ_NV_SO"
  START WITH 1
  INCREMENT BY 1
  NOCACHE
  NOCYCLE;

CREATE TABLE "NHANVIEN" (
  "MANV"      VARCHAR2(20)   PRIMARY KEY,   -- vd: 20250001, 20250002
  "MATK"      NUMBER,
  "MACH"      NUMBER,
  "CCCD"      VARCHAR2(20),
  "HOTEN"     VARCHAR2(100)  NOT NULL,
  "NGAYSINH"  DATE,
  "GIOITINH"  VARCHAR2(10),
  "SDT"       VARCHAR2(15),
  "DIACHI"    VARCHAR2(255),
  "CHUCVU"    VARCHAR2(50),
  CONSTRAINT "UQ_NHANVIEN_CCCD" UNIQUE ("CCCD")
);

-- =========================
-- 5. DANHMUCSANPHAM
-- =========================
CREATE TABLE "DANHMUCSANPHAM" (
  "MADM"      NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "MADM_CHA"  NUMBER,
  "TENDM"     VARCHAR2(100)  NOT NULL,
  "MOTA"      CLOB
);

-- =========================
-- 6. SANPHAM
-- =========================
CREATE TABLE "SANPHAM" (
  "MASP"        NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "MADM"        NUMBER,
  "TENSP"       VARCHAR2(200)  NOT NULL,
  "THUONGHIEU"  VARCHAR2(100),
  "MOTA"        CLOB,
  "ANH"         VARCHAR2(500),
  "TRANGTHAI"   VARCHAR2(20)
);

COMMENT ON COLUMN "SANPHAM"."TRANGTHAI" IS 'DangBan | NgungBan | HetHang';

-- =========================
-- 7. BIENTHE
-- =========================
CREATE TABLE "BIENTHE" (
  "MABIENTHE"   NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "MASP"        NUMBER,
  "SKU"         VARCHAR2(50),
  "BARCODE"     VARCHAR2(50),
  "MAUSAC"      VARCHAR2(50),
  "DUNGLUONG"   VARCHAR2(50),
  "KICHTHUOC"   VARCHAR2(50),
  "GIA_NHAP"    NUMBER(15,2),
  "GIA_BAN"     NUMBER(15,2),
  "TRANGTHAI"   VARCHAR2(20),
  CONSTRAINT "UQ_BIENTHE_SKU"     UNIQUE ("SKU"),
  CONSTRAINT "UQ_BIENTHE_BARCODE" UNIQUE ("BARCODE")
);

COMMENT ON COLUMN "BIENTHE"."TRANGTHAI" IS 'DangBan | NgungBan';

-- =========================
-- 8. SERIALNUMBER
-- =========================
CREATE TABLE "SERIALNUMBER" (
  "MASN"         NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "MABIENTHE"    NUMBER,
  "MACH"         NUMBER,
  "SERIAL_IMEI"  VARCHAR2(50)   NOT NULL,
  "TRANGTHAI"    VARCHAR2(20),
  CONSTRAINT "UQ_SN_SERIAL_IMEI" UNIQUE ("SERIAL_IMEI")
);

COMMENT ON COLUMN "SERIALNUMBER"."MACH"      IS 'Serial đang nằm ở cửa hàng nào. Null nếu đã bán';
COMMENT ON COLUMN "SERIALNUMBER"."TRANGTHAI" IS 'TonKho | DaBan | HongHoc';

-- =========================
-- 9. NHACUNGCAP
-- =========================
CREATE TABLE "NHACUNGCAP" (
  "MANCC"      NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "TENNCC"     VARCHAR2(100)  NOT NULL,
  "DIACHI"     VARCHAR2(255),
  "SDT"        VARCHAR2(15),
  "EMAIL"      VARCHAR2(100),
  "MASOTHUE"   VARCHAR2(20),
  "TRANGTHAI"  VARCHAR2(20),
  CONSTRAINT "UQ_NCC_MASOTHUE" UNIQUE ("MASOTHUE")
);

COMMENT ON COLUMN "NHACUNGCAP"."TRANGTHAI" IS 'HoatDong | DungHopTac';

-- =========================
-- 10. TONKHO (Composite PK)
-- =========================
CREATE TABLE "TONKHO" (
  "MACH"       NUMBER,
  "MABIENTHE"  NUMBER,
  "SOLUONG"    NUMBER(10)  DEFAULT 0  NOT NULL,
  CONSTRAINT "PK_TONKHO" PRIMARY KEY ("MACH", "MABIENTHE")
);

-- =========================
-- 11. PHIEUNHAP
-- =========================
CREATE TABLE "PHIEUNHAP" (
  "MAPN"       NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "MACH"       NUMBER,
  "MANCC"      NUMBER,
  "MANV"       VARCHAR2(20),
  "NGAYNHAP"   TIMESTAMP,
  "TONGTIEN"   NUMBER(15,2),
  "GHICHU"     VARCHAR2(500),
  "TRANGTHAI"  VARCHAR2(20)
);

COMMENT ON COLUMN "PHIEUNHAP"."MACH"      IS 'Nhập vào kho của cửa hàng nào';
COMMENT ON COLUMN "PHIEUNHAP"."TRANGTHAI" IS 'NhapKho | HuyPhieu';

-- =========================
-- 12. CHITIETPHIEUNHAP (Composite PK)
-- =========================
CREATE TABLE "CHITIETPHIEUNHAP" (
  "MAPN"       NUMBER,
  "MABIENTHE"  NUMBER,
  "SOLUONG"    NUMBER(10)    NOT NULL,
  "DONGIA"     NUMBER(15,2)  NOT NULL,
  CONSTRAINT "PK_CTPN" PRIMARY KEY ("MAPN", "MABIENTHE")
);

-- =========================
-- 13. PHIEUCHUYENKHO
-- =========================
CREATE TABLE "PHIEUCHUYENKHO" (
  "MAPCK"          NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "MACH_NGUON"     NUMBER,
  "MACH_DICH"      NUMBER,
  "MANV"           VARCHAR2(20),
  "NGAYCHUYENKHO"  TIMESTAMP,
  "GHICHU"         VARCHAR2(500),
  "TRANGTHAI"      VARCHAR2(20)
);

COMMENT ON COLUMN "PHIEUCHUYENKHO"."MACH_NGUON" IS 'Cửa hàng xuất hàng';
COMMENT ON COLUMN "PHIEUCHUYENKHO"."MACH_DICH"  IS 'Cửa hàng nhận hàng';
COMMENT ON COLUMN "PHIEUCHUYENKHO"."TRANGTHAI"  IS 'ChoDuyet | DaChuyenKho | HuyPhieu';

-- =========================
-- 14. CHITIETPHIEUCHUYENKHO (Composite PK)
-- =========================
CREATE TABLE "CHITIETPHIEUCHUYENKHO" (
  "MAPCK"      NUMBER,
  "MABIENTHE"  NUMBER,
  "SOLUONG"    NUMBER(10)  NOT NULL,
  CONSTRAINT "PK_CTPCK" PRIMARY KEY ("MAPCK", "MABIENTHE")
);

-- =========================
-- 15. KHACHHANG
-- =========================
CREATE TABLE "KHACHHANG" (
  "MAKH"        NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "HOTEN"       VARCHAR2(100)  NOT NULL,
  "SDT"         VARCHAR2(15),
  "EMAIL"       VARCHAR2(100),
  "NGAYSINH"    DATE,
  "GIOITINH"    VARCHAR2(10),
  "DIACHI"      VARCHAR2(255),
  "NGAYDANGKY"  DATE,
  CONSTRAINT "UQ_KH_SDT" UNIQUE ("SDT")
);

-- =========================
-- 16. DIACHIGIAOHANG
-- =========================
CREATE TABLE "DIACHIGIAOHANG" (
  "MADCGH"    NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "MAKH"      NUMBER,
  "HOTEN_NN"  VARCHAR2(100)  NOT NULL,
  "SDT_NN"    VARCHAR2(15)   NOT NULL,
  "TINH"      VARCHAR2(50),
  "HUYEN"     VARCHAR2(50),
  "XA"        VARCHAR2(50),
  "DIACHICT"  VARCHAR2(255),
  "LOAI"      VARCHAR2(20),
  "MACDINH"   NUMBER(1)      DEFAULT 0
);

COMMENT ON COLUMN "DIACHIGIAOHANG"."LOAI" IS 'NhaRieng | VanPhong | Khac';

-- =========================
-- 17. VOUCHER
-- PK: VARCHAR2(50) — mã nhập tay, dùng luôn làm mã nhập khi thanh toán
-- =========================
CREATE TABLE "VOUCHER" (
  "MAVOUCHER"          VARCHAR2(50)   PRIMARY KEY,
  "TENVOUCHER"         VARCHAR2(200)  NOT NULL,
  "LOAI"               VARCHAR2(20)   NOT NULL,
  "GIATRI"             NUMBER(15,2)   NOT NULL,
  "GIATRI_TOI_DA"      NUMBER(15,2),
  "DIEUKIEN_TOITHIEU"  NUMBER(15,2)   DEFAULT 0,
  "SOLUONG"            NUMBER(10),
  "SOLUONG_DA_DUNG"    NUMBER(10)     DEFAULT 0,
  "NGAYBATDAU"         TIMESTAMP      NOT NULL,
  "NGAYHETHAN"         TIMESTAMP      NOT NULL,
  "TRANGTHAI"          VARCHAR2(20),
  "GHICHU"             VARCHAR2(500)
);

COMMENT ON COLUMN "VOUCHER"."MAVOUCHER"          IS 'Mã nhập tay: VD SALE11, BIRTHDAY2024';
COMMENT ON COLUMN "VOUCHER"."TENVOUCHER"         IS 'VD: Sale sinh nhật thương hiệu, Đôi đôi 2/2';
COMMENT ON COLUMN "VOUCHER"."LOAI"               IS 'PhanTram | SoTienCoDinh';
COMMENT ON COLUMN "VOUCHER"."GIATRI"             IS '10 = giảm 10% | 50000 = giảm 50k';
COMMENT ON COLUMN "VOUCHER"."GIATRI_TOI_DA"      IS 'Giảm tối đa — chỉ dùng khi LOAI = PhanTram. VD: 10% nhưng tối đa 200k';
COMMENT ON COLUMN "VOUCHER"."DIEUKIEN_TOITHIEU"  IS 'Giá trị đơn tối thiểu để áp dụng';
COMMENT ON COLUMN "VOUCHER"."SOLUONG"            IS 'Null = không giới hạn';
COMMENT ON COLUMN "VOUCHER"."TRANGTHAI"          IS 'HoatDong | TamDung | HetHan';

-- =========================
-- 18. HOADON
-- =========================
CREATE TABLE "HOADON" (
  "MAHD"          NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "MACH"          NUMBER,
  "MANV"          VARCHAR2(20),
  "MAKH"          NUMBER,
  "MADCGH"        NUMBER,
  "MAVOUCHER"     VARCHAR2(50),
  "NGAYLAP"       TIMESTAMP,
  "TONGTIEN"      NUMBER(15,2),
  "GIATRI_GIAM"   NUMBER(15,2)   DEFAULT 0,
  "PHIVANCHUYEN"  NUMBER(15,2)   DEFAULT 0,
  "PTTT"          VARCHAR2(20),
  "LOAIHD"        VARCHAR2(20),
  "TRANGTHAI"     VARCHAR2(20)
);

COMMENT ON COLUMN "HOADON"."MADCGH"        IS 'Null nếu bán tại quầy';
COMMENT ON COLUMN "HOADON"."MAVOUCHER"     IS 'Null nếu không dùng voucher';
COMMENT ON COLUMN "HOADON"."TONGTIEN"      IS 'Snapshot tại thời điểm chốt đơn';
COMMENT ON COLUMN "HOADON"."GIATRI_GIAM"   IS 'Số tiền thực tế đã giảm từ voucher';
COMMENT ON COLUMN "HOADON"."PTTT"          IS 'TienMat | ChuyenKhoan | QRCode | TraGop';
COMMENT ON COLUMN "HOADON"."LOAIHD"        IS 'TaiQuay | Online';
COMMENT ON COLUMN "HOADON"."TRANGTHAI"     IS 'ChoDuyet | DaXacNhan | DangGiao | HoanThanh | DaHuy';

-- =========================
-- 19. CHITIETHOADON (Composite PK)
-- =========================
CREATE TABLE "CHITIETHOADON" (
  "MAHD"     NUMBER,
  "MASN"     NUMBER,
  "DONGIA"   NUMBER(15,2)  NOT NULL,
  "GIAMGIA"  NUMBER(15,2)  DEFAULT 0,
  CONSTRAINT "PK_CTHD" PRIMARY KEY ("MAHD", "MASN")
);

-- =========================
-- 20. VANCHUYEN
-- =========================
CREATE TABLE "VANCHUYEN" (
  "MAVC"             NUMBER         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "MAHD"             NUMBER,
  "DONVIVANCHUYEN"   VARCHAR2(50),
  "MAVANDONTHUONG"   VARCHAR2(50),
  "PHIVANCHUYEN"     NUMBER(15,2),
  "TRANGTHAIVC"      VARCHAR2(20),
  "NGAYDUKIEN"       DATE,
  "NGAYGIAO"         TIMESTAMP,
  CONSTRAINT "UQ_VC_MAVANDONTHUONG" UNIQUE ("MAVANDONTHUONG")
);

COMMENT ON COLUMN "VANCHUYEN"."DONVIVANCHUYEN" IS 'GiaoHangNhanh | GiaoHangTietKiem | ViettelPost';
COMMENT ON COLUMN "VANCHUYEN"."TRANGTHAIVC"    IS 'ChoLayHang | DangVanChuyen | DaGiao | HoanHang';

PROMPT === 00_create_tables.sql: Tao bang hoan tat ===