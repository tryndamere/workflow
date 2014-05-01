package com.rocky.workflow.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringExpressionConvert {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringExpressionConvert.class);

    private String expression;
    private Map<String, Object> variable;

    public SpringExpressionConvert(String expression, Map<String, Object> variable) {
        this.expression = expression;
        this.variable = variable;
    }

    public Object resolverExpression() {
        if (expression != null) {
            String objectAndMethod = expression.split("\\{")[1].split("\\}")[0];
            if (objectAndMethod.contains(".")) {
                String[] objectAndMethods = objectAndMethod.split("\\.");
                Object bean = SpringUtils.getBean(objectAndMethods[0]);
                if (bean != null) {
                    String[] methodAndParams = objectAndMethods[1].split("\\(");
                    String[] params = methodAndParams[1].split("\\)")[0].split("\\,");
                    paramsResolver(params);
                    return reflectClassAndMethod(bean.getClass(), methodAndParams[0],
                            params, getTypes(params.length), bean);
                }
            } else {
                if (variable != null && variable.containsKey(objectAndMethod)) {
                    return variable.get(objectAndMethod).toString();
                }
            }
        }
        return expression;
    }

    private void paramsResolver(String[] params) {
        System.out.println(variable);
        for (int i = 0, size = params.length; i < size; i++) {
            String param = params[i];
            if (param.contains("'")) {
                params[i] = param.split("\\'")[1];
            } else {
                if (variable != null && variable.containsKey(param)) {
                    params[i] = variable.get(param).toString();
                }
            }
        }
    }

    /**
     * 初始化类型
     *
     * @param size
     * @return
     */
    private Class<?>[] getTypes(int size) {
        Class<?>[] clz = new Class[size];
        for (int i = 0; i < size; i++) {
            clz[i] = String.class;
        }
        return clz;
    }

    /**
     * 通过反射调用具体方法
     *
     * @param cls       需要调用的class
     * @param method    方法名
     * @param arguments 参数值
     * @param types     参数类型
     * @param instance  需要调用的class对象实例
     * @return
     */
    public final Object reflectClassAndMethod(Class cls, String method,
                                              String[] arguments, Class<?>[] types, Object instance) {
        LOGGER.debug("======================================");
        LOGGER.debug("method , arguments : {},{}", method, arguments);
        LOGGER.debug("======================================");
        Method callMethod = null;
        try {
            if (arguments[0] == null) {
                callMethod = cls.getMethod(method);
                if (callMethod.invoke(instance) != null)
                    return callMethod.invoke(instance);
            } else {
                callMethod = cls.getMethod(method, types);
                if (callMethod.invoke(instance, arguments) != null)
                    return callMethod.invoke(instance, arguments);
            }
        } catch (SecurityException e) {
            LOGGER.error("reflectClassAndMethod SecurityException message :" + e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("reflectClassAndMethod NoSuchMethodException message :" + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("reflectClassAndMethod IllegalArgumentException message :" + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error("reflectClassAndMethod IllegalAccessException message :" + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error("reflectClassAndMethod InvocationTargetException message :" + e.getMessage(), e);
        }
        return null;
    }

    public static void main(String[] args) {

        String str = "'a'";
        System.out.println(str.split("\\'")[1]);
    }

}
