package com.example.contractalert.biz.Entity;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

//员工合同到期实体类
@Data
@TableName("esd_contract_alert")
public class EsdContractAlert {

    @ExcelProperty("合同ID")
    @ColumnWidth(20) // 加宽列，避免显示不全
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ExcelProperty("员工工号")
    private String empNo;

    @ExcelProperty("员工姓名")
    private String empName;

    @ExcelProperty("部门名称")
    private String deptName;

    @ExcelProperty("合同开始日期")
    @ColumnWidth(18)
    private LocalDate contractStart;

    @ExcelProperty("合同结束日期")
    @ColumnWidth(18)
    private LocalDate contractEnd;

    @ExcelProperty("剩余天数")
    private Integer remainDays;

    @ExcelProperty("预警等级")
    private Integer alertLevel;

    @ExcelProperty("是否续签")
    private Integer isRenewed;

    @ExcelProperty("跟进状态")
    private String followStatus;

    @ExcelProperty("跟进备注")
    private String followNote;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private LocalDateTime createTime;

    @ExcelProperty("更新时间")
    @ColumnWidth(20)
    private LocalDateTime updateTime;

    @ExcelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ExcelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ExcelIgnore // 不导出这个字段
    private Integer delFlag;
}