package com.flyingh;

import com.flyingh.service.UserService;
import com.flyingh.vo.User;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.junit.Test;

import javax.xml.xpath.XPathExpressionException;
import java.lang.reflect.InvocationTargetException;

public class Demo {
    @Test
    public void test3() {
        User user = new User();
        User proxy = (User) Enhancer.create(User.class, (InvocationHandler) (o, method, objects) -> {
            System.out.println("before");
            Object result = method.invoke(user, objects);
            System.out.println(result);
            System.out.println("after");
            return result;
        });
        proxy.sayHello("Flyingh");
    }

    @Test
    public void test2() {
    }

    @Test
    public void test() throws ClassNotFoundException, InstantiationException, XPathExpressionException, IllegalAccessException, InvocationTargetException {
        UserService userService = BeanFactory.getBean("userService", UserService.class);
        userService.login("Flyingh");
        userService.logout("Flyingh");
        System.out.println("***********");
        User user = BeanFactory.getBean("user", User.class);
        user.sayHello("haha");
    }
}
