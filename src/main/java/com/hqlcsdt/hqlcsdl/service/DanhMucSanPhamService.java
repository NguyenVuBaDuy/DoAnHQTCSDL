package com.hqlcsdt.hqlcsdl.service;

import com.hqlcsdt.hqlcsdl.dto.response.DanhMucResponse;
import com.hqlcsdt.hqlcsdl.entity.DanhMucSanPham;
import com.hqlcsdt.hqlcsdl.repository.DanhMucSanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DanhMucSanPhamService {

    private final DanhMucSanPhamRepository danhMucSanPhamRepository;

    @Transactional(readOnly = true)
    public List<DanhMucResponse> getAllCategories() {
        return danhMucSanPhamRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DanhMucResponse> getCategoryTree() {
        List<DanhMucSanPham> allCategories = danhMucSanPhamRepository.findAll();
        
        // Map all entities to DTOs first
        Map<Long, DanhMucResponse> dtoMap = allCategories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toMap(DanhMucResponse::getMaDm, dto -> dto));

        List<DanhMucResponse> rootCategories = new ArrayList<>();

        // Build the tree
        for (DanhMucSanPham entity : allCategories) {
            DanhMucResponse dto = dtoMap.get(entity.getMaDm());
            if (entity.getDanhMucCha() == null) {
                rootCategories.add(dto);
            } else {
                DanhMucResponse parentDto = dtoMap.get(entity.getDanhMucCha().getMaDm());
                if (parentDto != null) {
                    if (parentDto.getChildren() == null) {
                        parentDto.setChildren(new ArrayList<>());
                    }
                    parentDto.getChildren().add(dto);
                }
            }
        }

        return rootCategories;
    }

    private DanhMucResponse mapToResponse(DanhMucSanPham entity) {
        return DanhMucResponse.builder()
                .maDm(entity.getMaDm())
                .tenDm(entity.getTenDm())
                .moTa(entity.getMoTa())
                .maDmCha(entity.getDanhMucCha() != null ? entity.getDanhMucCha().getMaDm() : null)
                .tenDmCha(entity.getDanhMucCha() != null ? entity.getDanhMucCha().getTenDm() : null)
                .build();
    }
}
