package com.markerhub.handleData;


import com.markerhub.entity.Permission;
import com.markerhub.entity.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class userData implements Serializable {

        private Integer id;

        private String username;

        private String email;

       public List<Role> roles;
    }
