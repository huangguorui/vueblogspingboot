package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Custom;
import com.markerhub.entity.RolePermission;
import com.markerhub.entity.Tags;
import com.markerhub.service.CustomService;
import com.markerhub.service.TagsService;
import com.markerhub.shiro.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-08-05
 */
@RestController
@RequestMapping("/custom")
public class CustomController {
    @Autowired
    CustomService customService ;

    @RequiresAuthentication
    @GetMapping("/list")
    public Result blogs(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size) {

        Page page =  new Page(currentPage, size);

        IPage pageData = customService.page(page, new QueryWrapper<Custom>().orderByDesc("id"));

        return Result.succ(pageData);
    }



    @PostMapping("/save")
    public Result edit(@Validated @RequestBody Custom custom, HttpServletRequest request ) {

        Custom temp = null;
        if(custom.getId() != null) {
            temp = customService.getById(custom.getId());
        } else {
            temp = new Custom();
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(1);
        }
        custom.setIp(getIpAddress(request));
        BeanUtil.copyProperties(custom, temp, "id","created");
        customService.saveOrUpdate(temp);
        return Result.succ(null);

    }

    @RequiresAuthentication
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        for (Integer id:ids){
            customService.removeById(id);
        }

        return Result.succ(null);
    }
    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址。
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串
     * @param request
     * @return
     */
    private static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip= inet.getHostAddress();
            }
        }
        return ip;
    }
}
