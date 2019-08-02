package com.mmall.util;


import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        //====序列化
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);

        //忽略空Bean转json的错误
        //空Bean指的是，对象的所有属性都没被赋值
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        //所有的日期格式都统一为 yyyy-MM-dd HH:mm:ss 的格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //====反序列化
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    //=====序列化
    public static <T> String obj2String(T obj){
        if (obj == null)
            return null;
        try {
            return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to String error",e);
            return null;
        }
    }

    //返回格式化好的json字符串,Pretty建议仅在调试时使用
    public static <T> String obj2StringPretty(T obj){
        if (obj == null)
            return null;
        try {
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to String error",e);
            return null;
        }
    }


    //=====反序列化

    /**
     * 该方法可将字符串反序列化为对象，但是只是属性都相同，其对象的地址不同.且该方法无法解决反序列化为容器的字符串
     */
    public static <T> T string2Obj(String str,Class<T> clazz){ //第一个T表示这是一个泛型方法，第二个T表示返回值是T类型,第三个T限制class类型
        if (!StringUtils.isNotEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)str : objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            log.warn("Parse String to object error",e);
            return null;
        }
    }

    /**
     * 以下两个反序列话方法，更加通用！！！！！！！！！！！！
     * @return
     */
    //方法更通用，需要将上述方法的class替换成TypeReference类型
    public static <T> T string2Obj(String str, TypeReference<T> typeReference){ //注意TypeReference是org.codehaus.jackson.type.TypeReference包下的
        if (!StringUtils.isNotEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.warn("Parse String to object error",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str,Class<?> collectionClass,Class<?>... elementClasses){     //参数中要用？，因为具体完全未知，且不一定与T相同
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (IOException e) {
            log.warn("Parse String to object error",e);
            return null;
        }
    }



    public static void main(String[] args) {
//        User u1 = new User();
//        u1.setId(1);
//        u1.setEmail("whb@mmall.com");
//
//        User u2 = new User();
//        u2.setId(2);
//        u2.setEmail("whb2@mmall.com");
//
//        String user1Json = JsonUtil.obj2String(u1);
//        String user1JsonPretty = JsonUtil.obj2StringPretty(u1);
//
//        log.info("user1Json:{}",user1Json);
//        log.info("user1JsonPretty:{}",user1JsonPretty);
//
//        List<User> list = new ArrayList<>();
//        list.add(u1);
//        list.add(u2);
//        String userListStr = JsonUtil.obj2StringPretty(list);
//
//        log.info("==========");
//        log.info(userListStr);
//
//        List<User> userListObj = JsonUtil.string2Obj(userListStr,List.class);//这样会变成LinkedHashMap
//
//        List<User> userListObj2 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {});
//
//        List<User> userListObj3 = JsonUtil.string2Obj(userListStr, List.class,User.class);
//
//
//        User user = JsonUtil.string2Obj(user1Json,User.class);
//        System.out.println("end");

    }
}
