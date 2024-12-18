package top.lll.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class R<T> {
    private int code;       // 状态码，例如 200 表示成功
    private String message; // 返回消息
    private List<T> data;         // 数据内容


    public static <T> R<T> success(T data) {
        return R.success(new ArrayList<>(Arrays.asList(data)));
    }

    // 静态方法封装统一返回格式
    public static <T> R<T> success(List<T> data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage("Success");
        r.setData(data);
        return r;
    }

    public static <T> R<T> error() {
        R<T> r = new R<>();
        r.setCode(500);
        r.setMessage("error");
        return r;
    }
}
