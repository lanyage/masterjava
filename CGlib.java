package test;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
class CGLib {
    static class Demo {
        public void sayName() {
            System.out.println("demo super class!!!");
        }
    }

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Demo.class);
        enhancer.setUseCache(false);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return methodProxy.invokeSuper(o, objects);
            }
        });
        Demo demo = (Demo) enhancer.create();
        System.out.println(demo.getClass().getSimpleName());
    }
}
