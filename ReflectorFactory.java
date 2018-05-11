package Register;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试类Demo
 */
class Demo {
    private int id;
    private String name;

    private Demo() {

    }

    public Demo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

/**
 * 反射器,根据类类型来封装其对应的构造器,属性和getter和setter方法
 */
class Reflector {
    private Constructor<?> defaultConstructor;
    private Map<String, MyMethodInvoker> getMethds;

    public Reflector(Class clazz) {
        System.out.println("new Reflector.");
        this.getMethds = new HashMap<>();
        this.addDefaultConstructor(clazz);
        this.addGetMethods(clazz);
    }

    public Constructor<?> getDefaultConstructor() {
        return defaultConstructor;
    }

    public Map<String, MyMethodInvoker> getGetMethds() {
        return getMethds;
    }

    /**
     * 添加默认的构造器
     *
     * @param type
     */
    private void addDefaultConstructor(Class<?> type) {
        Constructor<?>[] constructor = type.getDeclaredConstructors();
        Constructor<?>[] cons$ = constructor;
        for (int i = 0; i < cons$.length; i++) {
            Constructor<?> con = cons$[i];
            int paramTypesLen$ = con.getParameterTypes().length;
            if (paramTypesLen$ == 0) {
                if (!con.isAccessible()) {
                    con.setAccessible(true);
                }
                this.defaultConstructor = con;
            }
        }
    }

    private void addGetMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        Method[] methods$ = methods;
        int methodLen$ = methods$.length;
        for (int i$ = 0; i$ < methodLen$; i$++) {
            String methodName$ = methods$[i$].getName();
            if (methodName$.indexOf("get") > -1) {
                this.getMethds.put(methodName$, new MyMethodInvoker(methods$[i$]));
            }
        }
    }
}

/**
 * 用来封装方法,并且调用Method的invoke方法
 */
class MyMethodInvoker {
    private Method method;
    public MyMethodInvoker(Method method) {
        this.method = method;
    }
    public Object invoke(Object obj, Object... args) {
        Object result = null;
        try {
            result = method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}

/**
 * 反射器工厂
 */
class ReflectorFactory {
    /** 将类类型和其对应的反射器封装起来 */
   private Map<Class<?>, Reflector> localCache = new HashMap<>();
   public Reflector newReflector(Class<?> clazz) {
       Reflector reflector = localCache.get(clazz);
       if(reflector == null) {
           reflector = new Reflector(clazz);
           localCache.put(clazz, reflector);
           return reflector;
       }
       return reflector;
   }
}

class Main {
    public static void main(String[] args) {
       ReflectorFactory reflectorFactory = new ReflectorFactory();
       for (int i = 0; i < 1; i++) {
          reflectorFactory.newReflector(Demo.class);
       }
    }
}
