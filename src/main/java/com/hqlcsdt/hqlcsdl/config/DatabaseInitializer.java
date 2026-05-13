package com.hqlcsdt.hqlcsdl.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DatabaseInitializer — Tự động khởi tạo database từ các file SQL.
 *
 * <p>Cơ chế hoạt động:</p>
 * <ul>
 *   <li>Lần đầu chạy (chưa có table nào): chạy tất cả file SQL theo thứ tự 00 → 06</li>
 *   <li>Các lần sau (đã có table): bỏ qua, không chạy lại</li>
 *   <li>Reset mode (app.db.reset=true): xóa toàn bộ → tạo lại từ đầu</li>
 * </ul>
 *
 * <p>Hỗ trợ Oracle PL/SQL: tách block bằng dấu "/" trên dòng riêng,
 * lọc bỏ lệnh PROMPT (chỉ dùng cho SQL*Plus).</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final DataSource dataSource;

    @Value("${app.db.init.enabled:true}")
    private boolean initEnabled;

    @Value("${app.db.reset:false}")
    private boolean resetDatabase;

    /**
     * Danh sách file SQL được chạy theo đúng thứ tự.
     * Thứ tự rất quan trọng vì các file sau phụ thuộc vào file trước.
     */
    private static final String[] SQL_FILES = {
            "db/00_create_tables.sql",
            "db/01_foreign_keys.sql",
            "db/02_sequences.sql",
            "db/03_triggers.sql",
            "db/04_stored_procedures.sql",
            "db/05_stored_functions.sql",
            "db/06_data_sample.sql"
    };

    private static final String RESET_SQL_FILE = "db/reset_all.sql";

    @PostConstruct
    public void initialize() {
        if (!initEnabled) {
            log.info("╔══════════════════════════════════════════════════════════╗");
            log.info("║  DB Init: DISABLED (app.db.init.enabled=false)          ║");
            log.info("╚══════════════════════════════════════════════════════════╝");
            return;
        }

        try {
            // === CHẾ ĐỘ RESET ===
            if (resetDatabase) {
                log.warn("╔══════════════════════════════════════════════════════════╗");
                log.warn("║  ⚠️  CẢNH BÁO: ĐANG RESET TOÀN BỘ DATABASE!            ║");
                log.warn("║  Tất cả tables, sequences, triggers, procedures,       ║");
                log.warn("║  functions sẽ bị XÓA và TẠO LẠI TỪ ĐẦU.               ║");
                log.warn("╚══════════════════════════════════════════════════════════╝");

                // Bước 1: Xóa toàn bộ
                executeSqlFile(RESET_SQL_FILE);
                log.info("✅ Reset database hoàn tất — đã xóa toàn bộ đối tượng.");

                // Bước 2: Tạo lại từ đầu
                executeAllSqlFiles();

                log.warn("╔══════════════════════════════════════════════════════════╗");
                log.warn("║  ⚠️  QUAN TRỌNG: Hãy tắt thuộc tính reset ngay!        ║");
                log.warn("║                                                        ║");
                log.warn("║  Vào file application.properties và đặt:               ║");
                log.warn("║      app.db.reset=false                                ║");
                log.warn("║                                                        ║");
                log.warn("║  Nếu không tắt, LẦN SAU CHẠY LẠI SẼ BỊ RESET LẠI!    ║");
                log.warn("╚══════════════════════════════════════════════════════════╝");
                return;
            }

            // === CHẾ ĐỘ BÌNH THƯỜNG ===
            if (isDatabaseEmpty()) {
                log.info("╔══════════════════════════════════════════════════════════╗");
                log.info("║  DB Init: Phát hiện database trống — khởi tạo lần đầu  ║");
                log.info("╚══════════════════════════════════════════════════════════╝");
                executeAllSqlFiles();
            } else {
                log.info("╔══════════════════════════════════════════════════════════╗");
                log.info("║  DB Init: Database đã tồn tại — bỏ qua khởi tạo       ║");
                log.info("╚══════════════════════════════════════════════════════════╝");
            }

        } catch (Exception e) {
            log.error("❌ Lỗi khi khởi tạo database!", e);
            throw new RuntimeException("Không thể khởi tạo database. Kiểm tra kết nối và file SQL.", e);
        }
    }

    /**
     * Kiểm tra database có trống không bằng cách đếm số table của user hiện tại.
     */
    private boolean isDatabaseEmpty() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM user_tables")) {
            rs.next();
            int tableCount = rs.getInt(1);
            log.info("📊 Số bảng hiện có trong database: {}", tableCount);
            return tableCount == 0;
        }
    }

    /**
     * Chạy tất cả file SQL theo thứ tự đã định nghĩa.
     */
    private void executeAllSqlFiles() throws Exception {
        log.info("🚀 Bắt đầu chạy {} file SQL...", SQL_FILES.length);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < SQL_FILES.length; i++) {
            String file = SQL_FILES[i];
            log.info("📄 [{}/{}] Đang chạy: {}", i + 1, SQL_FILES.length, file);
            executeSqlFile(file);
            log.info("✅ [{}/{}] Hoàn tất: {}", i + 1, SQL_FILES.length, file);
        }

        long elapsed = System.currentTimeMillis() - startTime;
        log.info("🎉 Khởi tạo database hoàn tất trong {}ms", elapsed);
    }

    /**
     * Đọc và thực thi một file SQL.
     * <p>
     * Xử lý đặc biệt cho Oracle:
     * <ul>
     *   <li>PL/SQL block (BEGIN...END; /) — tách bằng dấu "/" trên dòng riêng</li>
     *   <li>CREATE OR REPLACE TRIGGER/PROCEDURE/FUNCTION — tách bằng "/"</li>
     *   <li>Câu lệnh DDL/DML thường — tách bằng ";"</li>
     *   <li>Lọc bỏ PROMPT (lệnh SQL*Plus, không hợp lệ qua JDBC)</li>
     *   <li>Lọc bỏ comment lines (--)</li>
     * </ul>
     */
    private void executeSqlFile(String resourcePath) throws Exception {
        ClassPathResource resource = new ClassPathResource(resourcePath);

        String content;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            content = reader.lines().collect(Collectors.joining("\n"));
        }

        List<String> statements = parseSqlStatements(content);

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Tắt autocommit để kiểm soát transaction
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            try {
                for (String sql : statements) {
                    try {
                        stmt.execute(sql);
                    } catch (SQLException e) {
                        // Log lỗi nhưng tiếp tục chạy (một số lệnh DROP có thể fail nếu object chưa tồn tại)
                        log.warn("⚠️  Lỗi khi chạy SQL trong {}: {} | SQL (100 ký tự đầu): {}",
                                resourcePath, e.getMessage(),
                                sql.length() > 100 ? sql.substring(0, 100) + "..." : sql);
                    }
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        }
    }

    /**
     * Phân tích nội dung file SQL thành danh sách các câu lệnh riêng lẻ.
     * <p>
     * Thuật toán:
     * <ol>
     *   <li>Duyệt từng dòng</li>
     *   <li>Bỏ qua comment (--) và PROMPT</li>
     *   <li>Nếu đang trong PL/SQL block → gom cho đến khi gặp "/" trên dòng riêng</li>
     *   <li>Nếu là câu lệnh thường → tách bằng ";"</li>
     * </ol>
     */
    private List<String> parseSqlStatements(String content) {
        List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();
        boolean insidePlSqlBlock = false;

        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();

            // Bỏ qua dòng trống
            if (trimmed.isEmpty()) {
                if (insidePlSqlBlock) {
                    currentStatement.append("\n");
                }
                continue;
            }

            // Bỏ qua comment thuần túy
            if (trimmed.startsWith("--")) {
                continue;
            }

            // Bỏ qua lệnh PROMPT (chỉ dùng cho SQL*Plus)
            if (trimmed.toUpperCase().startsWith("PROMPT")) {
                continue;
            }

            // Bỏ qua lệnh COMMENT ON (không cần thiết cho JDBC)
            // Thực ra COMMENT ON là SQL hợp lệ, ta vẫn chạy được
            // Nên giữ lại

            // Dấu "/" trên dòng riêng = kết thúc PL/SQL block
            if (trimmed.equals("/")) {
                if (insidePlSqlBlock && currentStatement.length() > 0) {
                    String sql = currentStatement.toString().trim();
                    if (!sql.isEmpty()) {
                        statements.add(sql);
                    }
                    currentStatement.setLength(0);
                    insidePlSqlBlock = false;
                }
                continue;
            }

            // Phát hiện bắt đầu PL/SQL block
            String upper = trimmed.toUpperCase();
            if (!insidePlSqlBlock &&
                    (upper.startsWith("BEGIN") ||
                     upper.startsWith("DECLARE") ||
                     upper.startsWith("CREATE OR REPLACE TRIGGER") ||
                     upper.startsWith("CREATE OR REPLACE PROCEDURE") ||
                     upper.startsWith("CREATE OR REPLACE FUNCTION") ||
                     upper.startsWith("CREATE OR REPLACE PACKAGE") ||
                     upper.startsWith("CREATE TRIGGER") ||
                     upper.startsWith("CREATE PROCEDURE") ||
                     upper.startsWith("CREATE FUNCTION"))) {
                // Nếu có statement đang chờ, flush nó
                if (currentStatement.length() > 0) {
                    flushSimpleStatements(currentStatement.toString(), statements);
                    currentStatement.setLength(0);
                }
                insidePlSqlBlock = true;
                currentStatement.append(line);
                continue;
            }

            if (insidePlSqlBlock) {
                // Trong PL/SQL block, gom tất cả vào
                currentStatement.append("\n").append(line);
            } else {
                // Câu lệnh SQL thường
                currentStatement.append("\n").append(line);

                // Nếu dòng kết thúc bằng ";" → flush
                if (trimmed.endsWith(";")) {
                    flushSimpleStatements(currentStatement.toString(), statements);
                    currentStatement.setLength(0);
                }
            }
        }

        // Flush phần còn lại (nếu có)
        if (currentStatement.length() > 0) {
            String remaining = currentStatement.toString().trim();
            if (!remaining.isEmpty()) {
                if (insidePlSqlBlock) {
                    statements.add(remaining);
                } else {
                    flushSimpleStatements(remaining, statements);
                }
            }
        }

        return statements;
    }

    private void flushSimpleStatements(String sql, List<String> statements) {
        // Xóa inline comment (-- ...) trên mỗi dòng trước khi split
        String[] lines = sql.split("\n");
        StringBuilder cleaned = new StringBuilder();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("--")) continue; // bỏ dòng comment thuần
            // Strip inline comment: tìm -- ngoài string literal
            int commentIdx = findInlineCommentIndex(trimmed);
            if (commentIdx >= 0) {
                trimmed = trimmed.substring(0, commentIdx).trim();
            }
            if (!trimmed.isEmpty()) {
                cleaned.append(trimmed).append("\n");
            }
        }

        String[] parts = cleaned.toString().split(";");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                statements.add(trimmed);
            }
        }
    }

    /**
     * Tìm vị trí "--" nằm ngoài string literal (ngoài dấu nháy đơn).
     * Trả về -1 nếu không có inline comment.
     */
    private int findInlineCommentIndex(String line) {
        boolean inString = false;
        for (int i = 0; i < line.length() - 1; i++) {
            char c = line.charAt(i);
            if (c == '\'') {
                inString = !inString;
            }
            if (!inString && c == '-' && line.charAt(i + 1) == '-') {
                return i;
            }
        }
        return -1;
    }
}
