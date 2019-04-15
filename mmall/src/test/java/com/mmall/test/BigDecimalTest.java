package com.mmall.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @Author: HongbinW
 * @Date: 2019/4/14 21:03
 * @Version 1.0
 * @Description:
 */
public class BigDecimalTest {
    @Test
    public void test1(){
        System.out.println(0.05+0.01);
        System.out.println(1.0-0.42);
        System.out.println(4.015*100);
        System.out.println(123.3/100);
//        0.060000000000000005
//        0.5800000000000001
//        401.49999999999994
//        1.2329999999999999
        //结果有出入，用BigDecimal来解决此问题
    }

    @Test
    public void test2(){
        BigDecimal b1 = new BigDecimal(0.05);
        BigDecimal b2 = new BigDecimal(0.01);
        System.out.println(b1.add(b2));
        //0.06000000000000000298372437868010820238851010799407958984375
        //比上面的还可怕
        //假设现在有0.06元，而购物车中就0.05和0.01元的商品，导致无法购买
    }

    @Test
    public void test3(){
        BigDecimal b1 = new BigDecimal("0.05");
        BigDecimal b2 = new BigDecimal("0.01");
        System.out.println(b1.add(b2));
        //0.06
    }


}
