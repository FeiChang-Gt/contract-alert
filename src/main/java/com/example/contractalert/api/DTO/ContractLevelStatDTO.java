package com.example.contractalert.api.DTO;
import lombok.Data;

/**
 * 4.等级统计出参
 */
@Data
public class ContractLevelStatDTO {
    /**
     * 预警等级 1正常 2预警 3紧急
     */
    private Integer level;
    /**
     * 等级名称
     */
    private String levelName;
    /**
     * 数量
     */
    private Long count;
}
