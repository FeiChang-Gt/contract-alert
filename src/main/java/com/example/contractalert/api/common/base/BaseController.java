package com.example.contractalert.api.common.base;

import com.example.contractalert.api.common.result.R;
//父类工具，帮助统一返回格式（使用R，给R封装，使得子类只用ok（）就可以返回规范格式）
public class  BaseController {

    protected <T> R<T> ok() {
        return R.success();
    }

    //成功信息
    protected <T> R<T> ok(String message) {
        return R.success(message);
    }

    //成功时间
    protected <T> R<T> ok(T data) {
        return R.success(data);
    }

    //未成功
    protected <T> R<T> fail(String message) {
        return R.fail(message);
    }
}
