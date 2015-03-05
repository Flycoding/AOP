package com.flyingh;

import com.flyingh.annotation.Log;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BeanFactory {
    public static final String CONFIG_FILE_NAME = "beans.xml";
    public static final String CLASS_ATTRIBUTE_NAME = "class";
    public static final String XPATH_FORMAT = "//bean[@id='%s']";
    public static final Class<Log> ANNOTATION_CLASS = Log.class;
    private static final Map<String, Object> CACHE = new HashMap<>();

    public static final <T> T getBean(String beanId, Class<? extends T> superOrInterfaceClass) throws XPathExpressionException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        T result = (T) CACHE.get(beanId);
        if (result != null) {
            return result;
        }
        String className = getClassNameById(beanId);
        Class<?> cls = Class.forName(className);
        if (!superOrInterfaceClass.isAssignableFrom(cls)) {
            throw new ClassCastException();
        }
        Object obj = cls.newInstance();
        if (!superOrInterfaceClass.isAnnotationPresent(ANNOTATION_CLASS)) {
            result = superOrInterfaceClass.cast(obj);
            CACHE.put(beanId, result);
            return result;
        }
        result = newProxyInstance(obj, superOrInterfaceClass);
        CACHE.put(beanId, result);
        return result;
    }

    private static String getClassNameById(String beanId) throws XPathExpressionException {
        InputSource source = new InputSource(BeanFactory.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
        Element element = (Element) XPathFactory.newInstance().newXPath().evaluate(String.format(XPATH_FORMAT, beanId), source, XPathConstants.NODE);
        return element.getAttribute(CLASS_ATTRIBUTE_NAME);
    }

    private static <T> T newProxyInstance(Object obj, Class<? extends T> superOrInterfaceClass) throws IllegalAccessException, InvocationTargetException {
        return superOrInterfaceClass.isInterface() ? (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), (proxy, method, args) -> {
            return handleInvocation(obj, method, args);
        }) : (T) Enhancer.create(superOrInterfaceClass, (InvocationHandler) (o, method, objects) -> {
            return handleInvocation(obj, method, objects);
        });
    }

    private static Object handleInvocation(Object obj, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
        String methodName = method.getName();
        if (Arrays.asList("toString", "hashCode", "equals").contains(methodName)) {
            return method.invoke(obj, args);
        }
        System.out.println("before:" + methodName);
        Object methodResult = method.invoke(obj, args);
        System.out.println("after:" + methodName);
        return methodResult;
    }
}
