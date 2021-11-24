package com.zlk.core.model.dto;

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
@ApiModel(value="订单DTO", description="订单DTO")
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "有效状态（0禁用，1启用）")
    private Integer status;
}
