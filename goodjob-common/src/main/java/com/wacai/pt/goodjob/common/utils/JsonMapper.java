package com.wacai.pt.goodjob.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 * 
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 * <pre>
 * +----------------------+-----------------------------------------------------+
 * | JSON Type            |  Java Type                                          |
 * +----------------------+-----------------------------------------------------+
 * | object               |  LinkedHashMap<String,Object>                       |
 * +----------------------+-----------------------------------------------------+
 * | array                |  ArrayList<Object>                                  |
 * +----------------------+-----------------------------------------------------+
 * | string               |  string                                             |
 * +----------------------+-----------------------------------------------------+
 * | number (no fraction) |  Integer, Long or BigInteger (smallest applicable)  |
 * +----------------------+-----------------------------------------------------+
 * | number (fraction)    |  Double (configurable to use BigDecimal)            |
 * +----------------------+-----------------------------------------------------+
 * | true|false           |  Boolean                                            |
 * +----------------------+-----------------------------------------------------+
 * | null                 |  null                                               |
 * +----------------------+-----------------------------------------------------+
 * </pre>
 *
 * Created by xuanwu on 2015/5/8
 */
public class JsonMapper {
    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    private ObjectMapper mapper;

    public JsonMapper() {
        this(null);
    }

    public JsonMapper(Include include) {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        if (include != null) {
            mapper.setSerializationInclusion(include);
        }
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static JsonMapper getNonNullBinder() {
        return NonNullSingletonHolder.nonNullBinder;
    }

    private static class NonNullSingletonHolder {
        public static final JsonMapper nonNullBinder = new JsonMapper(Include.NON_NULL);
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
     */
    public static JsonMapper nonEmptyMapper() {
        return new JsonMapper(Include.NON_EMPTY);
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
     */
    public static JsonMapper nonDefaultMapper() {
        return new JsonMapper(Include.NON_DEFAULT);
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    public String toJson(Object object) {

        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * 
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * 
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     * @see #fromJson(String, com.fasterxml.jackson.databind.JavaType)
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isNullOrBlank(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函数createCollectionType构造类型,然后调用本函数.
     * @see #createCollectionType(Class, Class...)
     */
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isNullOrBlank(jsonString)) {
            return null;
        }

        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 构造泛型的Collection Type如:
     * ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
     * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
     */
    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 当JSON里只含有Bean的部分属性时，更新一个已存在Bean，只覆盖该部分属性
     */
    public <T> T update(String jsonString, T object) {
        try {
            return (T) mapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    /**
     * 输出JSONP格式数据
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 设定是否使用Enum的toString函数来读写Enum，
     * 为false时使用Enum的name()的函数来读写Enum，默认为False
     * 注意本函数一定要在Mapper创建后, 所有的读写动作之前调用.
     */
    public void enableEnumUseToString() {
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }


    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
}
