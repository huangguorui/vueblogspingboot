package com.markerhub.handleData;


import com.markerhub.entity.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class imgData implements Serializable {


    private String imgUrl;
    private String imgText;

    private String imgName;

    }
