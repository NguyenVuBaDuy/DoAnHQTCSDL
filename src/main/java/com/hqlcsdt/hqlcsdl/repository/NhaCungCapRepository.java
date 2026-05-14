package com.hqlcsdt.hqlcsdl.repository;

import com.hqlcsdt.hqlcsdl.entity.NhaCungCap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Long> {
}
