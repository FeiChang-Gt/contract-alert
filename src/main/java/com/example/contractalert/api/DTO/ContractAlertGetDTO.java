package com.example.contractalert.api.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 3.1更新前查询前端入参
 */

@Data
public class ContractAlertGetDTO {
    @NotNull(message = "ID不能为空")
    private Long id;
}
