package com.mmall.common;


import com.google.common.collect.Sets;

import java.util.Set;

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

    public interface Cart{
        int CHECKED = 1;//购物车选中状态
        int UN_CHECKED = 0;//购物车未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }
    public enum ProductStatusEnum{
        ON_SALE(1,"在线");
        private String value;
        private int code;
        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ACE_DESC = Sets.newHashSet("price_desc","price_asc");

    }
}
