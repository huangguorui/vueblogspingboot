package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "摘要不能为空")
    private String description;
    @NotBlank(message = "内容不能为空")
    private String content;

    private LocalDateTime created;


    private String img;

    @TableField("indexImg")
    private String indexImg;


    @TableField("themeId")
    private int themeId;

    private int status;
    @NotBlank(message = "技术标签不能为空")

    private String tags;
    @NotBlank(message = "页数不能为空")

    private String pages;
    @NotBlank(message = "价格不能为空")

    private String price;

    public Long setId() {
        return  id;
    }

    public Long setUserId() {
        return userId;
    }

    public String setTitle() {
        return title;
    }

    public String setDescription() {
        return description;
    }

    public String setContent() {
        return content;
    }

    public LocalDateTime setCreated() {
        return created;
    }

    public String setImg() {
        return img;
    }

    public String setIndexImg() {
        return indexImg;
    }

    public int setThemeId() {
        return themeId;
    }

    public int setStatus() {
        return status;
    }

    public String setTags() {
        return  tags;
    }

    public String setPages() {
        return pages;
    }

    public String setPrice() {
        return price;
    }


}
