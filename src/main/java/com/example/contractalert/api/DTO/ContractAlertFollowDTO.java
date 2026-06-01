package com.example.contractalert.api.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 3.2更新跟进前端入参
 */
@Data
public class ContractAlertFollowDTO {

    @NotNull(message = "ID不能为空")
    private Long id;

    @NotNull(message = "是否续签不能为空")
    private Integer isRenewed;
    //不续签备注
    private String followNote;
    //跟进状态（如果不续签必填）
    private String followStatus;
}