package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    private static final String COOKIE_DOMAIN = ".mmall.com";       //写在一级域名下
    private static final String COOKIE_NAME = "mmall_login_token";

    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck = new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");        //表示设置在根目录。  如果指定目录比如说test，则test及其子目录下会读到信息，其他目录不可以
        ck.setHttpOnly(true); //不允许使用脚本访问cookie
        ck.setMaxAge(60*60*24*365);   //-1表示永久，如果maxAge不设置的话，cookie就不会写入硬盘，而是写在内存，只在当前页面有效

        log.info("write cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);

    }

    //our cookie domain=".mmall.com"        一级域名下的domain

    //a:A.mmall.com                     cookie:domain=A.mmall.com;path="/"
    //b:B.mmall.com                     cookie:domain=B.mmall.com;path="/"
    //c:A.mmall.com/test/cc             cookie:domain=A.mmall.com;path="/test/cc"
    //d:A.mmall.com/test/dd             cookie:domain=A.mmall.com;path="/test/dd"
    //e:A.mmall.com/test                cookie:domain=A.mmall.com;path="/test"

    //这样abcde均可以拿到这个cookie
    //a,b是两个二级域名，这样a拿不到b的cookie，b也拿不到a的cookie
    //c,d可以共享a的cookie，且c,d也可以共享e的cookie，但是c拿不到d的cookie，d也拿不到c的cookie，c和d也拿不到b的


    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if (cks != null){
            for (Cookie ck : cks){
                log.info("read cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                if (StringUtils.equals(ck.getName(),COOKIE_NAME)){  //StringUtils.equal，先做了空判断，才判断是否相等
                    log.info("return cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cks = request.getCookies();
        if (cks != null){
            for (Cookie ck : cks){
                if (StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0); //设置成0，表示删除此cookie

                    log.info("del cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }

    }
}
