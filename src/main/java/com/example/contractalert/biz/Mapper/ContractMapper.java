package com.example.contractalert.biz.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.contractalert.api.DTO.ContractLevelStatDTO;
import com.example.contractalert.biz.Entity.EsdContractAlert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractMapper extends BaseMapper<EsdContractAlert> {
    /**
     * 预警等级统计
     */
    List<ContractLevelStatDTO> levelStat();
}
