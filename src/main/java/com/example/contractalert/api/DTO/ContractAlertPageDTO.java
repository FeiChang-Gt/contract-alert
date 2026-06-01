package com.example.contractalert.api.DTO;

import lombok.Data;

/**
 * 2.分页查询前端入参
 */
@Data
public class ContractAlertPageDTO {
    /** 当前页码，默认1 */
    private Integer pageNum = 1;
    /** 每页条数，默认10 */
    private Integer pageSize = 10;

    /** 员工工号（模糊查询） */
    private String empNo;
    /** 员工姓名（模糊查询） */
    private String empName;
    /** 所属部门 */
    private String deptName;
    /** 预警等级：1正常 2预警 3紧急 */
    private Integer alertLevel;
    /** 是否已续签：0未续签 1已续签 */
    private Integer isRenewed;
}
