/**
 * Created by lanyage on 2018/5/6.
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 文件操作的所有方法
 */
interface Demo {
    int getStringLength(String str);
}

class Main2 {
    public void doSomething() {
        Object delegate = Proxy.newProxyInstance(Demo.class.getClassLoader(), new Class[]{Demo.class}, new MyProxyInvocationHandler(new Demo() {
            @Override
            public int getStringLength(String str) {
                return str.length();
            }
        }));
        Demo demo = (Demo)delegate;
        int len = demo.getStringLength("123");
        System.out.println(len);
    }

    public static void main(String[] args) {
        new Main2().doSomething();
    }

    private class MyProxyInvocationHandler implements InvocationHandler {
        private Object obj;

        public MyProxyInvocationHandler(Object obj) {
            this.obj = obj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(obj, args);
        }
    }
}

