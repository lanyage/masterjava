package Register;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lanyage on 2018/5/11.
 */
interface IDemo {
    String getName();

    void sayName(String name);
}

class Demo implements IDemo {
    public String getName() {
        return "demo";
    }

    public void sayName(String name) {
        System.out.println(name);
    }
}

class MapperProxy implements InvocationHandler {
    private Demo demo;
    private Map<String, Method> methodMap;

    public MapperProxy(Demo demo, Map<String, Method> methodMap) {
        this.demo = demo;
        this.methodMap = methodMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用代理的方法");
        /** 如果是基础方法例toString则不会 */
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            /** 可以返回其他对象方法所返回的数据,MapperProxy中的cachedMethod<Method,MapperMethod>就是这样的,
             * MapperMethod的execute方法就是包装SqlSession,然后会有需要执行的Method和Statement,
             * 使用SqlSession来执行Sql语句
             * */
            Method method1 = cachedMethod(method);
            return method1.getName();
        }
    }

    private Method cachedMethod(Method method) {
        this.methodMap.put(method.getName(), method);
        return method;
    }
}

public class MapperDemo {
    public static void main(String[] args) {
        MapperProxy mapperProxy = new MapperProxy(new Demo(), new HashMap<>());
        IDemo demo = (IDemo) Proxy.newProxyInstance(Demo.class.getClassLoader(), Demo.class.getInterfaces(), mapperProxy);
        demo.sayName("hello");
    }
}
