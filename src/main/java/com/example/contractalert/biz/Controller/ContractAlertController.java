package com.example.contractalert.biz.Controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.contractalert.api.DTO.*;
import com.example.contractalert.api.common.base.BaseController;
import com.example.contractalert.api.common.result.R;
import com.example.contractalert.biz.Entity.EsdContractAlert;
import com.example.contractalert.biz.Service.ContractAlertService;
import com.example.contractalert.biz.task.ContractAlertAutoUpdateTask;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //定义接口
@RequestMapping("/contract") //所以访问前缀
public class ContractAlertController extends BaseController {

    //service层调用
    @Resource
    private ContractAlertService contractAlertService;

    @Autowired
    private ContractAlertAutoUpdateTask contractAlertAutoUpdateTask;

    //录入
    @PostMapping("/add")
    //@Valid检查校验（NotBlank、NotNull）; @RequestBody接受前段传来的json数据
    public R<String> add(@Valid @RequestBody ContractAlertAddDTO dto) {
        contractAlertService.addContractAlert(dto);
        return ok("✅ 合同预警信息录入成功");
    }


    // 分页查询，查询是没请求体的，不用加@RequestBody
    @GetMapping("/page")
    public R<IPage<EsdContractAlert>> page(ContractAlertPageDTO dto) {
        IPage<EsdContractAlert> page = contractAlertService.page(dto);
        return ok(page);
    }

    // 根据ID查询（编辑回显）
    @GetMapping("/getById")
    public R<EsdContractAlert> getById(ContractAlertGetDTO dto) {
        EsdContractAlert entity = contractAlertService.getById(dto);
        return ok(entity);
    }

    //跟新更进
    @PostMapping("/follow")
    public R<String> follow(@RequestBody ContractAlertFollowDTO dto) {
        contractAlertService.follow(dto);
        return ok("跟进成功");
    }

    //等级统计
    @GetMapping("/levelStat")
    public R<List<ContractLevelStatDTO>> levelStat() {
        return ok(contractAlertService.levelStat());
    }

    //每日自动跟新（测试用的接口）
    @GetMapping("/refreshLevel")
    public R<String> refreshLevel() {
        contractAlertAutoUpdateTask.autoUpdateAlertLevel();
        return ok("刷新成功");
    }

    // 导出Excel
    @GetMapping("/export")
    //HttpServletResponse response:把文件直接给浏览器
    public void export(HttpServletResponse response) throws Exception {
        // 查询所有未删除数据
        List<EsdContractAlert> list = contractAlertService.exportList();

        //  【新增：限制最多导出500行】
        int maxLimit = 500;
        if (list.size() > maxLimit) {
            // 返回错误提示，不导出
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print("导出数据不能超过" + maxLimit + "条，请筛选后重试");
            return;
        }


        // 设置文件下载头
        response.setContentType("application/vnd.ms-excel"); //告诉浏览器这是excel不是网页
        response.setCharacterEncoding("utf-8"); //编码设置，防止乱码
        //设置下载时候的名字
        String fileName = java.net.URLEncoder.encode("合同预警列表", "UTF-8");
        //attachment:打开的是文件，不是网页
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 写出Excel
        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), EsdContractAlert.class)
                .sheet("合同数据")
                .doWrite(list);
        /**
         * EasyExcel.write(...)
         * 使用阿里的 EasyExcel 工具生成 Excel。
         * response.getOutputStream()
         * 把 Excel 直接写到浏览器下载流。
         * EsdContractAlert.class
         * 直接用你的实体类导出，不用 DTO。
         * .sheet("合同数据")
         * Excel 底部的 sheet 名称。
         * .doWrite(list)
         * 把查询到的数据写入 Excel 并下载。
         */
    }
}
