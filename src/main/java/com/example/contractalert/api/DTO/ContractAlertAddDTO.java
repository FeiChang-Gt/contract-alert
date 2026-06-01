package com.example.contractalert.api.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

/**
 * 1.录入前端入参
 */
@Data
public class ContractAlertAddDTO {

    @NotBlank(message = "员工工号不能为空") //校验穿的值不能为空，为空返回message值（用于字符串String）
    private String empNo;

    @NotBlank(message = "员工姓名不能为空") //校验穿的值不能为空，为空返回message值（用于字符串String）
    private String empName;

    private String deptName;

    @NotNull(message = "合同开始日期不能为空")//校验穿的值不能为空，为空返回message值（用于其他日期，数字，枚举）
    private LocalDate contractStart;

    @NotNull(message = "合同结束日期不能为空")//校验穿的值不能为空，为空返回message值（用于其他日期，数字，枚举）
    private LocalDate contractEnd;

    private Long createBy;
}
