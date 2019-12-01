package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: HongbinW
 * @Date: 2019/4/17 15:14
 * @Version 1.0
 * @Description:
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {


    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpServletRequest request, @RequestParam(value = "pageNum",defaultValue = "1")int pageNum, @RequestParam(value = " pageSize",defaultValue = "10")int pageSize){
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录");
//        }
//        String userJsonString = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonString,User.class);
//        if(user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            //填充增加产品的业务逻辑
//            return iOrderService.manageList(pageNum,pageSize);
//        }
//        return ServerResponse.createByErrorMessage("没有权限，仅限管理员登录");
        return iOrderService.manageList(pageNum,pageSize);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpServletRequest request, Long orderNo){
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录");
//        }
//        String userJsonString = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonString,User.class);
//        if(user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            //填充增加产品的业务逻辑
//            return iOrderService.manageDetail(orderNo);
//        }
//        return ServerResponse.createByErrorMessage("没有权限，仅限管理员登录");
        return iOrderService.manageDetail(orderNo);
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpServletRequest request, Long orderNo, @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,@RequestParam(value = " pageSize",defaultValue = "10")int pageSize){
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录");
//        }
//        String userJsonString = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonString,User.class);
//        if(user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            //填充增加产品的业务逻辑
//            return iOrderService.manageSearch(orderNo,pageNum,pageSize);
//        }
//        return ServerResponse.createByErrorMessage("没有权限，仅限管理员登录");
        return iOrderService.manageSearch(orderNo,pageNum,pageSize);
    }

    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpServletRequest request, Long orderNo){
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录");
//        }
//        String userJsonString = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonString,User.class);
//        if(user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            //填充增加产品的业务逻辑
//            return iOrderService.manageSendGoods(orderNo);
//        }
//        return ServerResponse.createByErrorMessage("没有权限，仅限管理员登录");
        return iOrderService.manageSendGoods(orderNo);

    }

}
