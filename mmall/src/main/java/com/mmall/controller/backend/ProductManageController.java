package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: HongbinW
 * @Date: 2019/4/11 10:36
 * @Version 1.0
 * @Description:
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     * 新增或更新产品
     * @param product：根据传入产品的参数是否有ID，若有ID则是更新产品，否则为新增产品
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpServletRequest request, Product product){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonString = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMessage("没有权限，仅限管理员登录");
    }

    /**
     * 更新产品上下架信息
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest request, Integer productId,Integer status){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonString = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }
        return ServerResponse.createByErrorMessage("没有权限，仅限管理员登录");
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpServletRequest request, Integer productId){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonString = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.manageProductDetail(productId);
        }
        return ServerResponse.createByErrorMessage("没有权限，仅限管理员登录");
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpServletRequest request, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonString = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("没有权限，仅限管理员登录");
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpServletRequest request,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonString = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 文件上传
     * @param file：SpringMVC的文件上传
     * @param request：根据Servlet上下文动态创建出相对路径
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file",required = false)MultipartFile file, HttpServletRequest request){
        //权限判断
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonString = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //业务
            String path = request.getSession().getServletContext().getRealPath("upload");   //会发布到webapp内
            String targetFileName = iFileService.upload(file,path);
            //跟前台约定，要把URL拼出来
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);
        }
        return ServerResponse.createByErrorMessage("没有权限，仅限管理员登录");
    }

    /**
     * 富文本图片的上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(@RequestParam(value = "upload_file",required = false)MultipartFile file, HttpServletRequest request,HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        //权限判断
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        String userJsonString = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求，我们使用的是simditor所以要按照simditor的要求进行返回
//        {
//            "success": true/false,
//            "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //业务
            String path = request.getSession().getServletContext().getRealPath("upload");   //会发布到webapp内
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            //修改response的Header
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }
}
