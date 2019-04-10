package com.mmall.common;

/**
 * @Author: HongbinW
 * @Date: 2019/4/8 16:38
 * @Version 1.0
 * @Description:
 */
public class Const {
    public static final String CURRENT_USER="currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    //使用内部接口类，把常量进行分组。效果：没有枚举那么繁重，又起到了分组的效果,且内部是常量
    public interface Role{
        int ROLE_CUSTOMER = 0;    //普通用户
        int ROLE_ADMIN = 1;       //管理员
    }
}
