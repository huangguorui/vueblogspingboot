package com.markerhub.controller;


//import cn.hutool.db.Page;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.markerhub.common.lang.Result;
//import com.markerhub.entity.Blog;
//import com.markerhub.service.BlogService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.management.Query;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.*;
import com.markerhub.handleData.roleData;
import com.markerhub.service.BlogService;
//import com.markerhub.util.ShiroUtil;
import com.markerhub.service.TagsService;
import com.markerhub.service.ThemeService;
import com.markerhub.shiro.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.omg.CORBA.Environment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.markerhub.handleData.imgData;
import com.markerhub.handleData.blogData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//研发权限控制系统
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-10
 */
@ResponseBody
@RestController
@RequestMapping("/article")
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    TagsService tagsService;

    @Autowired
    ThemeService themeService;

    /**
     * @api {post} /article/pic/:title
     * @apiDescription  图片上传
     * @apiName list
     * @apiParam {file} file 图片

     * @apiParamExample {json} Request-Example:
     * {
     *     "file":"",
     * }
     * @apiGroup articleGroup
     * @apiError userNotFound  The <code>id</code>
     * @apiSampleRequest /article/pic
     */
    @RequiresAuthentication
    @RequestMapping("/pic")
    @ResponseBody
    public Result uplodFile(@RequestParam("file") MultipartFile[] file) throws Exception {
        /*String originalFilename = file.getOriginalFilename();
        FileInputStream inputStream = (FileInputStream) file.getInputStream();*/
       /* System.out.println(inputStream);
        System.out.println(originalFilename);*/
        System.out.println("数组的长度"+file.length);

        List<imgData> imgList = new ArrayList<>();


//        String fileName = file[0].getOriginalFilename();
//        String filePath = "/uploads/";
//        File dest = new File(filePath + fileName);
//        try {
//            file[0].transferTo(dest);
//        } catch (IOException e) {
//        }
//        return Result.succ(null);

//
//
        String imgPath="";
        for (int i = 0; i < file.length; i++) {
            //获取图片名称。容易重复
//            String originalFilename = file[i].getOriginalFilename();



            //生成不重复的ID
            String originalFilename= UUID.randomUUID().toString()+getSuffix( file[i].getOriginalFilename());
            InputStream inputStream = file[i].getInputStream();

            File directory = new File("");// 参数为空
            String courseFile = directory.getCanonicalPath();
            System.out.println("path2: "+courseFile);
            FileOutputStream fo = new FileOutputStream(courseFile+"\\src\\main\\resources\\static\\uploads\\"+originalFilename);
            File f = new File(this.getClass().getResource("/").getPath());
            System.out.println("path1: "+f);
            FileOutputStream target = new FileOutputStream(f+"\\static\\uploads\\"+originalFilename);

            imgData add = new imgData();
            add.setImgName(originalFilename);
            add.setImgUrl(getUrl()+"/uploads/"+originalFilename);
            add.setImgText("/uploads/"+originalFilename);
            System.out.println("setImgUrl="+add.getImgUrl());
            System.out.println("database="+"/uploads/"+originalFilename);

            if(file.length-1==i){
                imgPath=imgPath+"/uploads/"+originalFilename;

            }else{
                imgPath+="/uploads/"+originalFilename+",";
            }


            fo.write( file[i].getBytes());
            target.write( file[i].getBytes());
            fo.close();
            target.close();
            System.out.println("originalFilename="+originalFilename);
            System.out.println("inputStream="+inputStream);
            imgList.add(add);
        }

        //写入项目列表中

//        Blog  temp = new Blog();
//        temp.setUserId(ShiroUtil.getProfile().getId());
//        temp.setCreated(LocalDateTime.now());
//        temp.setStatus(1);
//        temp.setContent("测试测试");
//        temp.setDescription("测试测试");
//        temp.setPages("测试测试");
//        temp.setPrice("测试测试");
//        temp.setTags("测试测试");
//        temp.setTitle("测试测试");
//
//        temp.setIndexImg("indexImg");
//        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");

        //测试
//        Tags tags = new Tags();
//        tags.setTagName(imgPath);
//        tagsService.save(tags);

//        blogService.saveOrUpdate(blog);


        System.out.println(getUrl());
        return Result.succ(imgList);

    }

    /**
     * 简化写法  获取图片后缀名称
     */
    private static String getSuffix(String str) {
        //获取文件的原始名称
        String originalFilename =str;//timg (1).jpg
        //获取文件的后缀名 .jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        return suffix;
//        System.out.println("suffix = " + suffix);
    }

    @Value("${server.port}")
    private int serverPort;
    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        this.serverPort
        return "http://"+address.getHostAddress() +":"+this.serverPort;
    }


    String[] imgList;


    /**
     * @api {get} /article/list/:title
     * @apiDescription  获取文章列表
     * @apiName list
     * @apiParam {currentPage} currentPage 当前页
     * @apiParam {size} size 分页大小
     * @apiParam {title} title 标题
     * @apiParamExample {json} Request-Example:
     * {
     *     "title":"caojing",
     *     "size":10,
     *     "currentPage":1,
     * }
     * @apiGroup fsdf
     * @apiError userNotFound  The <code>id</code>
     * @apiSampleRequest /article/list
     */

    @GetMapping("/list")
    public Result blogs(@RequestParam(defaultValue = "1") Integer currentPage,@RequestParam(defaultValue = "10") Integer size,String title) {

        Page page =  new Page(currentPage, size);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        if(title!=null){
            //eq精确匹配  like模糊匹配
            pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("id").like("title","title"));
        }else{
            pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("id"));

        }

        List<blogData> result = new ArrayList<>();
        List<Blog> blogList = pageData.getRecords();

        for (Blog blog : blogList) {
            blogData add = new blogData();
            add.setContent(blog.getContent());
            add.setId(blog.getId());

            add.setTitle(blog.getTitle());
            add.setCreated(blog.getCreated());

            add.setDescription(blog.getDescription());

            add.setImg(blog.getImg());
            add.setUserId(blog.getUserId());
//            add.setImgArr(blog.getImg());

            add.setPages(blog.getPages());
            add.setPrice(blog.getPrice());

            add.setIndexImg(blog.getIndexImg());

            add.setStatusText(blog.getStatus());
            add.setStatus(blog.getStatus());

            add.setTags(blog.getTags());
            add.setTagsArr(blog.getTags());
            List<Theme> themeList = themeService.selectByThemeId(blog.getThemeId());
            if (themeList.size()==0) {
                add.setThemeName("默认栏目");
            }
            for (Theme theme : themeList) {
                add.setThemeName(theme.getThemeName());

            }
            result.add(add);
        }




// BeanUtils.copyProperties(blog,blogList); 对象复制，待议

        pageData.setRecords(result);

        return Result.succ(pageData);
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable(name = "id") Long id){
        Blog blog = blogService.getById(id);
        Assert.notNull(blog,"该博客已被删除");

        //博客文件格式转换
        blogData add = new blogData();
        add.setContent(blog.getContent());
        add.setId(blog.getId());

        add.setTitle(blog.getTitle());
        add.setThemeId(blog.getThemeId());
        add.setCreated(blog.getCreated());

        add.setDescription(blog.getDescription());

        add.setImg(blog.getImg());
        add.setImgArr(blog.getImg());

        add.setPages(blog.getPages());
        add.setPrice(blog.getPrice());

        add.setIndexImg(blog.getIndexImg());

        add.setStatusText(blog.getStatus());
        add.setStatus(blog.getStatus());

        add.setTags(blog.getTags());
        add.setTagsArr(blog.getTags());
        List<Theme> themeList = themeService.selectByThemeId(blog.getThemeId());
        if (themeList.size()==0) {
            add.setThemeName("默认栏目");
        }
        System.out.println(themeList.size());
        for (Theme theme : themeList) {
            add.setThemeName(theme.getThemeName());

        }

        return Result.succ(add);
    }

    @RequiresAuthentication
    @PostMapping("/save")

//    @RequiresPermissions(value={"blog+edit"})
//    /blog/edit
//
//    /blog/edit  blog+edit
    //@RequiresPermissions(value={"user:a", "user:b"}, logical= Logical.AND)

    public Result edit(@Validated @RequestBody Blog blog) {
        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {

            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(1);
            BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
            blogService.saveOrUpdate(temp);

            return Result.succ(null);
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created");//, "status"
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        System.out.println(ids);
        for (Integer id:ids){
            blogService.removeById(id);
        }

        return Result.succ(null);
    }

}
