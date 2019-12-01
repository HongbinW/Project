package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.mmall.util.CookieUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.util.JsonUtil;
import com.mmall.common.Const;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor{
    //controller 处理之前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // o 是一个handler Method
        log.info("preHandle");
        //请求中Controller的方法名
        HandlerMethod handlerMetod = (HandlerMethod) o;
        String methodName = handlerMetod.getMethod().getName();
        String className = handlerMetod.getBean().getClass().getSimpleName();

        //解析参数,use for log
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            String mapKey = (String)entry.getKey();
            Object obj = entry.getValue();
            String mapValue = StringUtils.EMPTY;
            if (obj instanceof String[]){
                String[] strs = (String[])obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }


        if(StringUtils.equals(className,"UserManageController") && StringUtils.equals(methodName,"login")){
            log.info("权限拦截器，拦截到请求，className:{},methodName:{}",className,methodName);
            //如果是登录请求，就不打印参数了，因为参数里面有密码
            return true;

        }

        log.info("权限拦截器，拦截到请求，className:{},methodName:{},param:{}",className,methodName,requestParamBuffer.toString());

        //logic
        User user = null;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)){
            String userJsonString = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonString,User.class);
        }

        if (user == null || (user.getRole() != Const.Role.ROLE_ADMIN)){
            //因为我们最终是要返回一个ServerResponse对象，但是拦截器这几个方法的返回值均不是，因此可以通过重写response保证返回结果，很关键！！！
            httpServletResponse.reset();    //must to reset response,否则会报异常 getWriter() has alread been called for this reponse.
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");   //设置返回值类型，均为json接口

            PrintWriter out = httpServletResponse.getWriter();

            if (user == null){
                //由于富文本上传要返回的是Map，因此要单独处理
                if(StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success","false");
                    resultMap.put("msg","请登录管理员");
                    out.print(JsonUtil.obj2String(resultMap));
                }else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
            }else{
                if(StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtext_img_upload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success","false");
                    resultMap.put("msg","无权限操作");
                    out.print(JsonUtil.obj2String(resultMap));
                }else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("用户无权限操作")));
                }
            }

            out.flush();
            out.close();

            return false;
        }


        return true;
    }
    //controller 处理之后
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }
    //因为我们时前后端分离的，因此后端只提供接口，不返回ModelAndView，因此这一步处理也不会用到
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
