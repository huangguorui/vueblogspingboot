package com.markerhub.handleData;


import com.markerhub.entity.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class roleData implements Serializable {

        private Integer id;

        private String roleName;

        private String roleDesc;

       public List<Permission> permissions;
    }
