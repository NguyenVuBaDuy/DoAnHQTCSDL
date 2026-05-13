package com.hqlcsdt.hqlcsdl.repository;

import com.hqlcsdt.hqlcsdl.entity.Nhom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhomRepository extends JpaRepository<Nhom, Long> {
}
