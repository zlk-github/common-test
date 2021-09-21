package com.zlk.core.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @author  likuan.zhou
* @title:  User对象
* @description: User对象 (注释部分为未调试，需要添加配置文件，见https://my.oschina.net/654476371/blog/3054503)
* @date 2021-09-14
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_user")
@ApiModel(value="User对象", description="用户表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @ApiModelProperty(value = "有效状态（0禁用，1启用）")
    private Integer status;

    @ApiModelProperty(value = "创建者id")
    //@TableField( fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    //@TableField( fill = FieldFill.INSERT)
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新者id")
    //@TableField( fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    //@TableField( fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
