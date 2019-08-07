package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: HongbinW
 * @Date: 2019/4/10 16:09
 * @Version 1.0
 * @Description:
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse<Category> addCategory(String categoryName, @RequestParam(value = "parentId" , defaultValue = "0")int parentId, HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonString = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //增加我们处理分类的逻辑
            return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpServletRequest request,Integer categoryId,String categoryName){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonString = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //更新categoryName
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    /**
     * 获取当前节点的子节点信息
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonString = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()) {
            //查询子节点的category信息，不递归，保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest request,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonString = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonString,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()) {
            //查询当前节点的id和递归子节点的Id
            //0-->1--->2    如果传入1则返回2，如果传入0，则返回1,2
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }
}
