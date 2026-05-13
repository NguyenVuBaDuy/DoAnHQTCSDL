package com.hqlcsdt.hqlcsdl.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class để gọi Oracle Stored Procedures một cách gọn gàng.
 * Sử dụng pattern Builder để đăng ký tham số và thực thi procedure.
 *
 * Ví dụ sử dụng:
 * <pre>
 *   storedProcedureUtils.call("INSERT_CUAHANG")
 *       .input("p_TENCH", "Cửa hàng A", String.class)
 *       .input("p_SDT", "0901234567", String.class)
 *       .execute();
 * </pre>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StoredProcedureUtils {

    private final EntityManager entityManager;

    /**
     * Khởi tạo một ProcedureCall builder cho procedure có tên cho trước.
     *
     * @param procedureName Tên stored procedure
     * @return ProcedureCall builder
     */
    public ProcedureCall call(String procedureName) {
        return new ProcedureCall(entityManager, procedureName);
    }

    /**
     * Builder class giúp đăng ký tham số IN/OUT và thực thi stored procedure.
     */
    public static class ProcedureCall {

        private final EntityManager entityManager;
        private final String procedureName;

        /** Lưu thứ tự tham số IN: tên → { value, type } */
        private final LinkedHashMap<String, ParamInfo> inParams = new LinkedHashMap<>();

        /** Lưu thứ tự tham số OUT: tên → type */
        private final LinkedHashMap<String, Class<?>> outParams = new LinkedHashMap<>();

        ProcedureCall(EntityManager entityManager, String procedureName) {
            this.entityManager = entityManager;
            this.procedureName = procedureName;
        }

        /**
         * Đăng ký tham số IN.
         *
         * @param name  Tên tham số (khớp với procedure)
         * @param value Giá trị truyền vào (có thể null)
         * @param type  Kiểu dữ liệu Java (String.class, Long.class, …)
         * @return this (fluent)
         */
        public ProcedureCall input(String name, Object value, Class<?> type) {
            inParams.put(name, new ParamInfo(value, type));
            return this;
        }

        /**
         * Đăng ký tham số OUT.
         *
         * @param name Tên tham số OUT
         * @param type Kiểu dữ liệu Java mong đợi
         * @return this (fluent)
         */
        public ProcedureCall output(String name, Class<?> type) {
            outParams.put(name, type);
            return this;
        }

        /**
         * Thực thi stored procedure (không có tham số OUT).
         */
        public void execute() {
            StoredProcedureQuery query = buildQuery();
            query.execute();
        }

        /**
         * Thực thi stored procedure và trả về Map chứa các giá trị OUT.
         *
         * @return Map<tên_tham_số, giá_trị>
         */
        public Map<String, Object> executeWithOutput() {
            StoredProcedureQuery query = buildQuery();
            query.execute();

            Map<String, Object> result = new LinkedHashMap<>();
            for (String outName : outParams.keySet()) {
                result.put(outName, query.getOutputParameterValue(outName));
            }
            return result;
        }

        // ==================== PRIVATE ====================

        private StoredProcedureQuery buildQuery() {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery(procedureName);

            // Đăng ký tham số IN
            for (Map.Entry<String, ParamInfo> entry : inParams.entrySet()) {
                query.registerStoredProcedureParameter(entry.getKey(), entry.getValue().type, ParameterMode.IN);
            }

            // Đăng ký tham số OUT
            for (Map.Entry<String, Class<?>> entry : outParams.entrySet()) {
                query.registerStoredProcedureParameter(entry.getKey(), entry.getValue(), ParameterMode.OUT);
            }

            // Set giá trị tham số IN
            for (Map.Entry<String, ParamInfo> entry : inParams.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue().value);
            }

            return query;
        }

        /**
         * Record lưu thông tin một tham số IN.
         */
        private record ParamInfo(Object value, Class<?> type) {}
    }
}
