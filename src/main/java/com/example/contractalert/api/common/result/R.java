package com.example.contractalert.api.common.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//统一响应类
@Data
@NoArgsConstructor
@AllArgsConstructor

public class R<T> {

    private Integer code;

    private String message;

    private T data;

    // ====================== 成功 ======================
    public static <T> R<T> success() {
        return new R<>(200, "操作成功", null);
    }

    public static <T> R<T> success(String message) {
        return new R<>(200, message, null);
    }

    public static <T> R<T> success(T data) {
        return new R<>(200, "操作成功", data);
    }

    // ====================== 失败 ======================
    public static <T> R<T> fail(String message) {
        return new R<>(500, message, null);
    }
}

