package com.hqlcsdt.hqlcsdl.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DanhMucResponse {
    private Long maDm;
    private String tenDm;
    private String moTa;
    private Long maDmCha;
    private String tenDmCha;
    private List<DanhMucResponse> children;
}
