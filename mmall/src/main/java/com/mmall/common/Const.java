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

    //购物车
    public interface Cart{
        int CHECKED = 1;//购物车选中状态
        int UN_CHECKED = 0;//购物车未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }
    //商品上下架状态
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
    //产品排序
    public interface ProductListOrderBy{
        Set<String> PRICE_ACE_DESC = Sets.newHashSet("price_desc","price_asc");
    }
    //订单支付状态
    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");

        OrderStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }
    //支付宝回调状态
    public interface AlipayCallback{
        String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_CLOSED = "TRADE_CLOSED";

        String TRADE_SUCCESS = "TRADE_SUCCESS";
        String TRADE_FINISHED = "TRADE_FINISHED";
        String RESPONSE_FAILED = "RESPONSE_FAILED";
    }
    //支付平台选择
    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
    //支付方式
    public enum PaymentTypeEnum{
        ONLINE_PAY(1,"在线支付");

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        //根据传入的code获取对应枚举
        public static PaymentTypeEnum codeOf(int code){
            for(PaymentTypeEnum paymentTypeEnum : values()){
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }
}
