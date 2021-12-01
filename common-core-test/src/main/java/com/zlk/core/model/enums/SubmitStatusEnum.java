package com.zlk.core.model.enums;

/**
 * TODO
 *
 * @author likuan.zhou
 * @date 2021/11/29/029 9:11
 */
public enum SubmitStatusEnum {
    /**
     * 回滚（失败）
     */
    ROLLBACK(2, "回滚（失败）"),
    /**
     * 提交（成功）
     */
    COMMIT(1, "提交（成功）"),
    /**
     * 对应tb_basic_config表
     */
    UNKNOWN(2, "未知（失败）");

    private final Integer code;
    private final String info;

    SubmitStatusEnum(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
