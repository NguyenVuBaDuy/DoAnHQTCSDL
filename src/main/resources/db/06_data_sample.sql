-- ============================================================
-- FILE: 06_data_sample.sql
-- DESC: Dữ liệu mẫu cho hệ thống quản lý chuỗi cửa hàng
-- NOTE: Chạy SAU 00 → 05
--       NHOM, TAIKHOAN, NHANVIEN được seed qua Java (DataSeeder)
--       để mã hóa password đúng cách bằng BCrypt.
--
-- Quy ước PK:
--   GENERATED ALWAYS AS IDENTITY → KHÔNG truyền cột PK
--   MANV (VARCHAR2)              → để NULL, trigger tự sinh YYYYxxxx
--   MAVOUCHER (VARCHAR2)         → nhập tay
-- ============================================================

-- =========================
-- 1. CỬA HÀNG
-- PK: MACH — auto identity (1, 2, ...)
-- =========================
INSERT INTO "CUAHANG" ("TENCH", "DIACHI", "SDT", "EMAIL", "NGAYKHAITRUONG", "TRANGTHAI")
VALUES ('TechStore Quận 1', '123 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP.HCM', '0281234567', 'q1@techstore.vn', TO_DATE('2023-01-15','YYYY-MM-DD'), 'HoatDong');

INSERT INTO "CUAHANG" ("TENCH", "DIACHI", "SDT", "EMAIL", "NGAYKHAITRUONG", "TRANGTHAI")
VALUES ('TechStore Quận 7', '456 Nguyễn Thị Thập, Phường Tân Phong, Quận 7, TP.HCM', '0289876543', 'q7@techstore.vn', TO_DATE('2023-06-01','YYYY-MM-DD'), 'HoatDong');

INSERT INTO "CUAHANG" ("TENCH", "DIACHI", "SDT", "EMAIL", "NGAYKHAITRUONG", "TRANGTHAI")
VALUES ('TechStore Thủ Đức', '789 Võ Văn Ngân, P.Linh Chiểu, TP.Thủ Đức, TP.HCM', '0281112233', 'thuduc@techstore.vn', TO_DATE('2024-03-10','YYYY-MM-DD'), 'HoatDong');

COMMIT;

-- =========================
-- 2. NHÀ CUNG CẤP
-- PK: MANCC — auto identity (1, 2, 3, ...)
-- =========================
INSERT INTO "NHACUNGCAP" ("TENNCC", "DIACHI", "SDT", "EMAIL", "MASOTHUE", "TRANGTHAI")
VALUES ('Công ty TNHH Phân phối Apple Việt Nam', '100 Tôn Đức Thắng, Q.1, TP.HCM', '0281001001', 'info@applevn.com', '0301234567', 'HoatDong');

INSERT INTO "NHACUNGCAP" ("TENNCC", "DIACHI", "SDT", "EMAIL", "MASOTHUE", "TRANGTHAI")
VALUES ('Công ty CP Samsung Vina', '200 Điện Biên Phủ, Q.Bình Thạnh, TP.HCM', '0281002002', 'sales@samsungvina.vn', '0302345678', 'HoatDong');

INSERT INTO "NHACUNGCAP" ("TENNCC", "DIACHI", "SDT", "EMAIL", "MASOTHUE", "TRANGTHAI")
VALUES ('Công ty TNHH Xiaomi Việt Nam', '300 Cách Mạng Tháng 8, Q.10, TP.HCM', '0281003003', 'contact@xiaomivn.com', '0303456789', 'HoatDong');

COMMIT;

-- =========================
-- 3. DANH MỤC SẢN PHẨM
-- PK: MADM — auto identity
-- Identity values: 1,2,3 (cấp 1) → 4,5 (cấp 2)
-- =========================
-- Cấp 1
INSERT INTO "DANHMUCSANPHAM" ("MADM_CHA", "TENDM", "MOTA")
VALUES (NULL, 'Điện thoại', 'Smartphone các hãng');                -- MADM = 1

INSERT INTO "DANHMUCSANPHAM" ("MADM_CHA", "TENDM", "MOTA")
VALUES (NULL, 'Laptop', 'Laptop văn phòng và gaming');              -- MADM = 2

INSERT INTO "DANHMUCSANPHAM" ("MADM_CHA", "TENDM", "MOTA")
VALUES (NULL, 'Phụ kiện', 'Ốp lưng, sạc, tai nghe');              -- MADM = 3

-- Cấp 2 (tham chiếu MADM cấp 1 bằng NUMBER)
INSERT INTO "DANHMUCSANPHAM" ("MADM_CHA", "TENDM", "MOTA")
VALUES (1, 'iPhone', 'Dòng điện thoại Apple');                     -- MADM = 4

INSERT INTO "DANHMUCSANPHAM" ("MADM_CHA", "TENDM", "MOTA")
VALUES (1, 'Samsung Galaxy', 'Dòng điện thoại Samsung');           -- MADM = 5

COMMIT;

-- =========================
-- 4. SẢN PHẨM
-- PK: MASP — auto identity (1, 2, 3, 4, 5)
-- FK: MADM → NUMBER
-- =========================
INSERT INTO "SANPHAM" ("MADM", "TENSP", "THUONGHIEU", "MOTA", "ANH", "TRANGTHAI")
VALUES (4, 'iPhone 15 Pro Max', 'Apple', 'iPhone 15 Pro Max chip A17 Pro, camera 48MP', 'iphone15promax.jpg', 'DangBan');        -- MASP = 1

INSERT INTO "SANPHAM" ("MADM", "TENSP", "THUONGHIEU", "MOTA", "ANH", "TRANGTHAI")
VALUES (4, 'iPhone 15', 'Apple', 'iPhone 15 chip A16, Dynamic Island', 'iphone15.jpg', 'DangBan');                                -- MASP = 2

INSERT INTO "SANPHAM" ("MADM", "TENSP", "THUONGHIEU", "MOTA", "ANH", "TRANGTHAI")
VALUES (5, 'Samsung Galaxy S24 Ultra', 'Samsung', 'Galaxy S24 Ultra chip Snapdragon 8 Gen 3, S-Pen', 'galaxys24ultra.jpg', 'DangBan');  -- MASP = 3

INSERT INTO "SANPHAM" ("MADM", "TENSP", "THUONGHIEU", "MOTA", "ANH", "TRANGTHAI")
VALUES (2, 'MacBook Air M3', 'Apple', 'MacBook Air chip M3, 13 inch, Liquid Retina', 'macbookairm3.jpg', 'DangBan');              -- MASP = 4

INSERT INTO "SANPHAM" ("MADM", "TENSP", "THUONGHIEU", "MOTA", "ANH", "TRANGTHAI")
VALUES (3, 'AirPods Pro 2', 'Apple', 'AirPods Pro 2 USB-C, chống ồn chủ động', 'airpodspro2.jpg', 'DangBan');                    -- MASP = 5

COMMIT;

-- =========================
-- 5. BIẾN THỂ
-- PK: MABIENTHE — auto identity (1..8)
-- FK: MASP → NUMBER
-- =========================
INSERT INTO "BIENTHE" ("MASP", "SKU", "BARCODE", "MAUSAC", "DUNGLUONG", "KICHTHUOC", "GIA_NHAP", "GIA_BAN", "TRANGTHAI")
VALUES (1, 'IPH15PM-256-BLK', 'VN001001001', 'Đen Titan',   '256GB', NULL, 28000000, 34990000, 'DangBan');    -- MABIENTHE = 1

INSERT INTO "BIENTHE" ("MASP", "SKU", "BARCODE", "MAUSAC", "DUNGLUONG", "KICHTHUOC", "GIA_NHAP", "GIA_BAN", "TRANGTHAI")
VALUES (1, 'IPH15PM-256-WHT', 'VN001001002', 'Trắng Titan', '256GB', NULL, 28000000, 34990000, 'DangBan');    -- MABIENTHE = 2

INSERT INTO "BIENTHE" ("MASP", "SKU", "BARCODE", "MAUSAC", "DUNGLUONG", "KICHTHUOC", "GIA_NHAP", "GIA_BAN", "TRANGTHAI")
VALUES (1, 'IPH15PM-512-BLK', 'VN001001003', 'Đen Titan',   '512GB', NULL, 33000000, 40990000, 'DangBan');    -- MABIENTHE = 3

INSERT INTO "BIENTHE" ("MASP", "SKU", "BARCODE", "MAUSAC", "DUNGLUONG", "KICHTHUOC", "GIA_NHAP", "GIA_BAN", "TRANGTHAI")
VALUES (2, 'IPH15-128-PNK',   'VN001002001', 'Hồng',        '128GB', NULL, 18000000, 22990000, 'DangBan');    -- MABIENTHE = 4

INSERT INTO "BIENTHE" ("MASP", "SKU", "BARCODE", "MAUSAC", "DUNGLUONG", "KICHTHUOC", "GIA_NHAP", "GIA_BAN", "TRANGTHAI")
VALUES (3, 'SGS24U-256-BLK',  'VN002001001', 'Đen Phantom', '256GB', NULL, 25000000, 33990000, 'DangBan');    -- MABIENTHE = 5

INSERT INTO "BIENTHE" ("MASP", "SKU", "BARCODE", "MAUSAC", "DUNGLUONG", "KICHTHUOC", "GIA_NHAP", "GIA_BAN", "TRANGTHAI")
VALUES (3, 'SGS24U-512-GRY',  'VN002001002', 'Xám Titan',   '512GB', NULL, 30000000, 39990000, 'DangBan');    -- MABIENTHE = 6

INSERT INTO "BIENTHE" ("MASP", "SKU", "BARCODE", "MAUSAC", "DUNGLUONG", "KICHTHUOC", "GIA_NHAP", "GIA_BAN", "TRANGTHAI")
VALUES (4, 'MBA-M3-256-SLV',  'VN003001001', 'Bạc',         '256GB', '13 inch', 24000000, 27990000, 'DangBan');  -- MABIENTHE = 7

INSERT INTO "BIENTHE" ("MASP", "SKU", "BARCODE", "MAUSAC", "DUNGLUONG", "KICHTHUOC", "GIA_NHAP", "GIA_BAN", "TRANGTHAI")
VALUES (5, 'APP2-USBC',       'VN004001001', 'Trắng',       NULL,    NULL, 4500000, 6790000, 'DangBan');          -- MABIENTHE = 8

COMMIT;

-- =========================
-- 6. SERIAL NUMBER
-- PK: MASN — auto identity (1..12)
-- FK: MABIENTHE, MACH → NUMBER
-- =========================
-- CH001 (MACH = 1)
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (1, 1, '353456789012345', 'TonKho');   -- MASN=1
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (1, 1, '353456789012346', 'TonKho');   -- MASN=2
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (2, 1, '353456789012347', 'TonKho');   -- MASN=3
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (3, 1, '353456789012348', 'TonKho');   -- MASN=4
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (4, 1, '353456789012349', 'TonKho');   -- MASN=5
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (5, 1, '354567890123456', 'TonKho');   -- MASN=6
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (8, 1, '356789012345678', 'TonKho');   -- MASN=7

-- CH002 (MACH = 2)
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (1, 2, '353456789012350', 'TonKho');   -- MASN=8
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (5, 2, '354567890123457', 'TonKho');   -- MASN=9
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (6, 2, '354567890123458', 'TonKho');   -- MASN=10
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (7, 2, '355678901234567', 'TonKho');   -- MASN=11
INSERT INTO "SERIALNUMBER" ("MABIENTHE","MACH","SERIAL_IMEI","TRANGTHAI") VALUES (8, 2, '356789012345679', 'TonKho');   -- MASN=12

COMMIT;

-- =========================
-- 7. TỒN KHO (Composite PK — MACH, MABIENTHE đều là NUMBER)
-- =========================
-- CH001 (MACH = 1)
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (1, 1, 2);
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (1, 2, 1);
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (1, 3, 1);
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (1, 4, 1);
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (1, 5, 1);
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (1, 8, 1);

-- CH002 (MACH = 2)
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (2, 1, 1);
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (2, 5, 1);
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (2, 6, 1);
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (2, 7, 1);
INSERT INTO "TONKHO" ("MACH","MABIENTHE","SOLUONG") VALUES (2, 8, 1);

COMMIT;

-- =========================
-- 8. KHÁCH HÀNG
-- PK: MAKH — auto identity (1, 2, 3)
-- =========================
INSERT INTO "KHACHHANG" ("HOTEN","SDT","EMAIL","NGAYSINH","GIOITINH","DIACHI","NGAYDANGKY")
VALUES ('Nguyễn Hoàng Nam','0971234567','nam.nh@gmail.com',TO_DATE('1992-04-15','YYYY-MM-DD'),'Nam','100 Lý Tự Trọng, Q.1, TP.HCM',TO_DATE('2024-01-10','YYYY-MM-DD'));

INSERT INTO "KHACHHANG" ("HOTEN","SDT","EMAIL","NGAYSINH","GIOITINH","DIACHI","NGAYDANGKY")
VALUES ('Trần Mai Phương','0982345678','phuong.tm@gmail.com',TO_DATE('1998-09-22','YYYY-MM-DD'),'Nu','200 Nguyễn Đình Chiểu, Q.3, TP.HCM',TO_DATE('2024-02-20','YYYY-MM-DD'));

INSERT INTO "KHACHHANG" ("HOTEN","SDT","EMAIL","NGAYSINH","GIOITINH","DIACHI","NGAYDANGKY")
VALUES ('Lê Văn Tùng','0993456789','tung.lv@gmail.com',TO_DATE('1995-12-01','YYYY-MM-DD'),'Nam','300 Võ Văn Tần, Q.3, TP.HCM',TO_DATE('2024-03-05','YYYY-MM-DD'));

COMMIT;

-- =========================
-- 9. ĐỊA CHỈ GIAO HÀNG
-- PK: MADCGH — auto identity (1, 2)
-- FK: MAKH → NUMBER
-- =========================
INSERT INTO "DIACHIGIAOHANG" ("MAKH","HOTEN_NN","SDT_NN","TINH","HUYEN","XA","DIACHICT","LOAI","MACDINH")
VALUES (1,'Nguyễn Hoàng Nam','0971234567','TP.HCM','Quận 1','Phường Bến Nghé','100 Lý Tự Trọng','NhaRieng',1);  -- MADCGH = 1

INSERT INTO "DIACHIGIAOHANG" ("MAKH","HOTEN_NN","SDT_NN","TINH","HUYEN","XA","DIACHICT","LOAI","MACDINH")
VALUES (2,'Trần Mai Phương','0982345678','TP.HCM','Quận 3','Phường 6','200 Nguyễn Đình Chiểu','VanPhong',1);     -- MADCGH = 2

COMMIT;

-- =========================
-- 10. VOUCHER
-- PK: MAVOUCHER — VARCHAR2(50), nhập tay
-- =========================
INSERT INTO "VOUCHER" ("MAVOUCHER","TENVOUCHER","LOAI","GIATRI","GIATRI_TOI_DA","DIEUKIEN_TOITHIEU","SOLUONG","SOLUONG_DA_DUNG","NGAYBATDAU","NGAYHETHAN","TRANGTHAI","GHICHU")
VALUES ('SALE10','Giảm 10% toàn bộ','PhanTram',10,200000,500000,100,0,TIMESTAMP '2024-01-01 00:00:00',TIMESTAMP '2025-12-31 23:59:59','HoatDong','Áp dụng cho đơn từ 500k');

INSERT INTO "VOUCHER" ("MAVOUCHER","TENVOUCHER","LOAI","GIATRI","GIATRI_TOI_DA","DIEUKIEN_TOITHIEU","SOLUONG","SOLUONG_DA_DUNG","NGAYBATDAU","NGAYHETHAN","TRANGTHAI","GHICHU")
VALUES ('FLAT50K','Giảm 50k cho đơn từ 1 triệu','SoTienCoDinh',50000,NULL,1000000,50,0,TIMESTAMP '2024-06-01 00:00:00',TIMESTAMP '2025-06-30 23:59:59','HoatDong','Flash sale tháng 6');

INSERT INTO "VOUCHER" ("MAVOUCHER","TENVOUCHER","LOAI","GIATRI","GIATRI_TOI_DA","DIEUKIEN_TOITHIEU","SOLUONG","SOLUONG_DA_DUNG","NGAYBATDAU","NGAYHETHAN","TRANGTHAI","GHICHU")
VALUES ('BIRTHDAY2024','Giảm 15% mừng sinh nhật','PhanTram',15,300000,200000,NULL,0,TIMESTAMP '2024-01-01 00:00:00',TIMESTAMP '2024-12-31 23:59:59','HoatDong','Voucher sinh nhật, không giới hạn lượt');

COMMIT;

-- =========================
-- 11. HÓA ĐƠN
-- PK: MAHD — auto identity
-- FK: MACH, MAKH, MADCGH → NUMBER; MANV → VARCHAR2; MAVOUCHER → VARCHAR2
-- NOTE: MANV tham chiếu giá trị do DataSeeder tạo.
--       Vì trigger sinh MANV = YYYY + seq, giá trị phụ thuộc năm chạy seeder.
--       Dưới đây dùng subquery để tham chiếu đúng.
-- =========================

-- HD mẫu 1 — Bán tại quầy CH001
INSERT INTO "HOADON" ("MACH","MANV","MAKH","MADCGH","MAVOUCHER","NGAYLAP","TONGTIEN","GIATRI_GIAM","PHIVANCHUYEN","PTTT","LOAIHD","TRANGTHAI")
VALUES (1, (SELECT "MANV" FROM "NHANVIEN" WHERE "HOTEN" = 'Trần Thị Bình'), 1, NULL, 'SALE10',
        TIMESTAMP '2024-06-15 10:30:00', 34990000, 200000, 0, 'TienMat', 'TaiQuay', 'HoanThanh');   -- MAHD = 1

-- HD mẫu 2 — Online CH002
INSERT INTO "HOADON" ("MACH","MANV","MAKH","MADCGH","MAVOUCHER","NGAYLAP","TONGTIEN","GIATRI_GIAM","PHIVANCHUYEN","PTTT","LOAIHD","TRANGTHAI")
VALUES (2, (SELECT "MANV" FROM "NHANVIEN" WHERE "HOTEN" = 'Lê Minh Châu'), 2, 2, NULL,
        TIMESTAMP '2024-07-20 14:00:00', 33990000, 0, 30000, 'ChuyenKhoan', 'Online', 'DaXacNhan');  -- MAHD = 2

COMMIT;

-- =========================
-- 12. CHI TIẾT HÓA ĐƠN (Composite PK — MAHD, MASN đều NUMBER)
-- =========================
INSERT INTO "CHITIETHOADON" ("MAHD","MASN","DONGIA","GIAMGIA")
VALUES (1, 1, 34990000, 200000);

INSERT INTO "CHITIETHOADON" ("MAHD","MASN","DONGIA","GIAMGIA")
VALUES (2, 8, 33990000, 0);

COMMIT;

-- =========================
-- 13. VẬN CHUYỂN
-- PK: MAVC — auto identity
-- FK: MAHD → NUMBER
-- =========================
INSERT INTO "VANCHUYEN" ("MAHD","DONVIVANCHUYEN","MAVANDONTHUONG","PHIVANCHUYEN","TRANGTHAIVC","NGAYDUKIEN","NGAYGIAO")
VALUES (2, 'GiaoHangNhanh', 'GHN123456789', 30000, 'DangVanChuyen', TO_DATE('2024-07-23','YYYY-MM-DD'), NULL);   -- MAVC = 1

COMMIT;

-- =========================
-- 14. PHIẾU NHẬP (mẫu)
-- PK: MAPN — auto identity
-- FK: MACH, MANCC → NUMBER; MANV → VARCHAR2
-- =========================
INSERT INTO "PHIEUNHAP" ("MACH","MANCC","MANV","NGAYNHAP","TONGTIEN","GHICHU","TRANGTHAI")
VALUES (1, 1, (SELECT "MANV" FROM "NHANVIEN" WHERE "HOTEN" = 'Phạm Thị Dung'),
        TIMESTAMP '2024-05-01 09:00:00', 84000000, 'Nhập lô iPhone 15 Pro Max đầu tiên', 'NhapKho');  -- MAPN = 1

COMMIT;

-- =========================
-- 15. CHI TIẾT PHIẾU NHẬP (Composite PK — MAPN, MABIENTHE)
-- =========================
INSERT INTO "CHITIETPHIEUNHAP" ("MAPN","MABIENTHE","SOLUONG","DONGIA")
VALUES (1, 1, 2, 28000000);

INSERT INTO "CHITIETPHIEUNHAP" ("MAPN","MABIENTHE","SOLUONG","DONGIA")
VALUES (1, 2, 1, 28000000);

COMMIT;

-- =========================
-- 16. PHIẾU CHUYỂN KHO (mẫu)
-- PK: MAPCK — auto identity
-- FK: MACH_NGUON, MACH_DICH → NUMBER; MANV → VARCHAR2
-- =========================
INSERT INTO "PHIEUCHUYENKHO" ("MACH_NGUON","MACH_DICH","MANV","NGAYCHUYENKHO","GHICHU","TRANGTHAI")
VALUES (1, 2, (SELECT "MANV" FROM "NHANVIEN" WHERE "HOTEN" = 'Phạm Thị Dung'),
        TIMESTAMP '2024-06-01 08:30:00', 'Chuyển hàng từ Q.1 sang Q.7', 'DaChuyenKho');  -- MAPCK = 1

COMMIT;

-- =========================
-- 17. CHI TIẾT PHIẾU CHUYỂN KHO (Composite PK — MAPCK, MABIENTHE)
-- =========================
INSERT INTO "CHITIETPHIEUCHUYENKHO" ("MAPCK","MABIENTHE","SOLUONG")
VALUES (1, 1, 1);

COMMIT;

PROMPT === 06_data_sample.sql: Insert du lieu mau hoan tat ===
