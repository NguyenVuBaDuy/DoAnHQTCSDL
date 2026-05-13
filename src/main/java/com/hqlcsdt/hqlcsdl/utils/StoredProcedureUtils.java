package com.hqlcsdt.hqlcsdl.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StoredProcedureUtils {

    private final EntityManager entityManager;

    /**
     * Khởi tạo StoredProcedureQuery
     *
     * @param procedureName Tên stored procedure
     * @return StoredProcedureQuery
     */
    public StoredProcedureQuery createQuery(String procedureName) {
        return entityManager.createStoredProcedureQuery(procedureName);
    }
}
