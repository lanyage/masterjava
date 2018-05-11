/**
 * 对象工厂,通过类类型来构建对象.
 */
class ObjectFactory {
    <T> T create(Class<T> clazz) {
        return create(clazz, null, null);
    }

    /**
     * @param clazz               类类型
     * @param constructorArgTypes 构再起参数类类型列表
     * @param constructorArgs     构造器参数列表
     * @param <T>                 类范型
     * @return
     */
    <T> T create(Class<T> clazz, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        /** 如果构造器有参数 */
        try {
            Constructor<T> constructor;
            if (constructorArgs != null && constructorArgTypes != null) {
                constructor = clazz.getDeclaredConstructor(constructorArgTypes.toArray(new Class<?>[constructorArgTypes.size()]));
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance(constructorArgs.toArray(new Object[constructorArgs.size()]));
            } else {
                constructor = clazz.getDeclaredConstructor(new Class[0]);
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance(new Object[0]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * 测试类
 */
class Demo {
    private String name;

    public Demo() {
        this("demo");
    }

    public Demo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Main {
    public static void main(String[] args) {
        ObjectFactory objectFactory = new ObjectFactory();
        Demo demo = objectFactory.create(Demo.class);
        System.out.println(demo.getName());

        List<Class<?>> constructorArgTypes = new ArrayList<>();
        constructorArgTypes.add(String.class);
        List<Object> constructorArgs = new ArrayList<>();
        constructorArgs.add("star");
        Demo demo1 = objectFactory.create(Demo.class, constructorArgTypes, constructorArgs);
        System.out.println(demo1.getName());
    }
}
