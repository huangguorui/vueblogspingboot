package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_custom")
public class Custom implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("peopleName")
    private String peopleName;

    @TableField("peoplePhone")
    private String peoplePhone;


    @TableField("peopleEmail")
   private String peopleEmail;

    @TableField("peopleDescribe")
    private String peopleDescribe;

    private Integer status;

    private LocalDateTime created;

    @TableField("porjectType")
    private String porjectType;

    private String ip;
}
