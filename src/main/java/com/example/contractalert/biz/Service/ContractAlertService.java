package com.example.contractalert.biz.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.contractalert.api.DTO.*;
import com.example.contractalert.biz.Entity.EsdContractAlert;
import com.example.contractalert.biz.Mapper.ContractMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

//业务层
@Service
public class ContractAlertService {

    //mapper层调用
    @Resource
    private ContractMapper  contractAlertMapper;

    /**
     * 录入
     * @param dto
     */
    public void addContractAlert(ContractAlertAddDTO dto) {

        // 1. 校验工号是否重复
        LambdaQueryWrapper<EsdContractAlert> wrapper = new LambdaQueryWrapper<>(); //查询数据库条件构造器
        //构造查询条件
        wrapper.eq(EsdContractAlert::getEmpNo, dto.getEmpNo()) //数据库里的员工号等于前端传来的员工号（eq：==）
                .eq(EsdContractAlert::getDelFlag, 0); //且数据没被删除（del_flag逻辑删除）
        //如果符合条件的大于0
        if (contractAlertMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("员工工号【" + dto.getEmpNo() + "】已存在，无法重复录入");
        }

        // 2. 计算剩余天数和预警等级
        long remainDays = ChronoUnit.DAYS.between(LocalDate.now(), dto.getContractEnd());//剩余天数（现在，合同到期）
        int alertLevel;
        if (remainDays > 30) {
            alertLevel = 1; // 绿色：>30天
        } else if (remainDays > 7) {
            alertLevel = 2; // 黄色：≤30天
        } else {
            alertLevel = 3; // 红色：≤7天
        }

        // 3. DTO转实体，设置默认值
        EsdContractAlert entity = new EsdContractAlert(); //创建一个实体类对象
        BeanUtils.copyProperties(dto, entity); //把dto复制到实体中（整体复制）
        entity.setRemainDays((int) remainDays); //算出来的天数强制类型转换后存入实体
        entity.setAlertLevel(alertLevel); //存入预警等级（前端根据数据变色）
        entity.setIsRenewed(0); //刚录入，默认未续签
        entity.setFollowStatus("待跟进"); //刚录入，显示待跟进
        entity.setDelFlag(0); //逻辑未删除

        // 4. 保存到数据库
        contractAlertMapper.insert(entity);
    }

    /**
     * 分页查询
     */
    public IPage<EsdContractAlert> page(ContractAlertPageDTO dto) {
        //1.创建分页查询的对象，传入当前页码，每页条数
        Page<EsdContractAlert> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        //2.mybatisPlus里面的构造查询条件
        LambdaQueryWrapper<EsdContractAlert> wrapper = new LambdaQueryWrapper<>();
        //3.只查询逻辑未删除的数据
        wrapper.eq(EsdContractAlert::getDelFlag, 0);
        //动态拼接查询条件（有值就拼接，没值就跳过）
        //判断是否传了工号
        if (StringUtils.hasText(dto.getEmpNo())) {
            //like:模糊查询，前面是数据库表的字段，后面是前端传的值
            wrapper.like(EsdContractAlert::getEmpNo, dto.getEmpNo());
        }
        //判断是否传了姓名，模糊查询
        if (StringUtils.hasText(dto.getEmpName())) {
            wrapper.like(EsdContractAlert::getEmpName, dto.getEmpName());
        }
        //部门精准查询
        if (StringUtils.hasText(dto.getDeptName())) {
            wrapper.eq(EsdContractAlert::getDeptName, dto.getDeptName());
        }
        //预警等级不为空
        if (dto.getAlertLevel() != null) {
            wrapper.eq(EsdContractAlert::getAlertLevel, dto.getAlertLevel());
        }
        //续签不为空
        if (dto.getIsRenewed() != null) {
            wrapper.eq(EsdContractAlert::getIsRenewed, dto.getIsRenewed());
        }
        //按照创建时间，最新创建的排最前面（继续在加条件）
        wrapper.orderByDesc(EsdContractAlert::getCreateTime);
        //把分页参数和查询条件一起给mp（固定顺序）
        return contractAlertMapper.selectPage(page, wrapper);
    }

    /**
     * 根据ID查询（编辑回显）
     */
    public EsdContractAlert getById(ContractAlertGetDTO dto) {
        // 1.构造查询条件
        LambdaQueryWrapper<EsdContractAlert> wrapper = new LambdaQueryWrapper<>();

        // 2.根据ID查询
        wrapper.eq(EsdContractAlert::getId, dto.getId())
                .eq(EsdContractAlert::getDelFlag, 0);

        // 3.返回单条数据
        return contractAlertMapper.selectOne(wrapper);
    }

    /**
     * 更新跟进
     */
    public void follow(ContractAlertFollowDTO dto) {
        //判断id是否存在
        LambdaQueryWrapper<EsdContractAlert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EsdContractAlert::getId, dto.getId())
                .eq(EsdContractAlert::getDelFlag, 0);


        EsdContractAlert exist = contractAlertMapper.selectOne(queryWrapper);
        if (exist == null) {
            throw new RuntimeException("该数据不存在或已删除");
        }

        //1.不续签且备注无值
        if (dto.getIsRenewed() == 0 && !StringUtils.hasText(dto.getFollowNote())) {
            throw new RuntimeException("不续签时必须填写跟进备注");
        }

        //2.构造更新条件
        LambdaUpdateWrapper<EsdContractAlert> wrapper = new LambdaUpdateWrapper<>();

        //3.根据ID更新（必传）
        wrapper.eq(EsdContractAlert::getId, dto.getId());

        //4.动态拼接更新字段（有值才更新，没值跳过）
        //是否续签
        if (dto.getIsRenewed() != null) {
            wrapper.set(EsdContractAlert::getIsRenewed, dto.getIsRenewed());
        }
        //跟进备注
        if (StringUtils.hasText(dto.getFollowNote())) {
            wrapper.set(EsdContractAlert::getFollowNote, dto.getFollowNote());
        }
        //跟进状态
        if (StringUtils.hasText(dto.getFollowStatus())) {
            wrapper.set(EsdContractAlert::getFollowStatus, dto.getFollowStatus());
        }

        //5.执行更新（固定写法）
        contractAlertMapper.update(null, wrapper);
    }

    /**
     * 等级统计
     * @return
     */
    public List<ContractLevelStatDTO> levelStat() {
        return contractAlertMapper.levelStat(); 
    }

    /**
     * 导出Excel：查询所有未删除数据
     */
    public List<EsdContractAlert> exportList(){
        //1.构造查询条件
        LambdaQueryWrapper<EsdContractAlert> wrapper = new LambdaQueryWrapper<>();

        //2.只查询未删除的数据
        wrapper.eq(EsdContractAlert::getDelFlag, 0);

        //3.按时间顺序排列（最新在最前）
        wrapper.orderByDesc(EsdContractAlert::getCreateTime);

        //4.返回列表
        return contractAlertMapper.selectList(wrapper);
    }


}
