package com.markerhub.handleData;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.markerhub.entity.Permission;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class blogData implements Serializable {

    private Long id;

    private String title;
    private String description;
    private String content;

    private LocalDateTime created;


    private String img;
    private String[] imgArr;

    private String tags;
    private String[] tagsArr;

    private String indexImg;

    private String pages;

    private String price;

    private int themeId;
    private String themeName;

    private int status;
    private String statusText;

    public void setTags(String tags) {
        this.tags = tags;
    }
    public void setTagsArr(String tags) {
        this.tagsArr = tags.split(",");
    }

    public void setImg(String img) {
        this.img = img;
    }
    public void setImgArr(String img) {
        this.imgArr = img.split(",");
    }

    public void setStatusText(int status) {
        this.statusText = status==1?"开启":"禁用";
    }




    }
