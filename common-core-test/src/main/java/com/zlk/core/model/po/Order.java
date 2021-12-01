package com.zlk.core.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zlk.core.model.enums.SubmitStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单DTO
 *
 * @author likuan.zhou
 * @date 2021/11/23/023 19:30
 */
@Data
@TableName("tt_order")
@ApiModel(value="订单", description="订单")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "有效状态（0禁用，1启用）")
    private Integer status;

    // 该处不需要，如果是非事务消息，可以做标记然后做定时任务补偿。
    // 事务消息自带补偿，所以不需要
    @ApiModelProperty(value = "Mq提交状态（0不成功，1成功，2未知(不成功)）")
    private SubmitStatusEnum submitStatus;
}
