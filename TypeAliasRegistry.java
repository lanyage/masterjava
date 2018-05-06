package Register;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lanyage on 2018/5/6.
 */
/** 用于注册类和类的别名*/
public class NameRegistry {
    /** 实际上注册是将想要注册的东西存放到一个hashMap中去 */
    private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<>();
    /** 构造器,默认注册一些类和类的别名,mybatis框架就会在程序运行的时候注册一些类如String.class */
    public NameRegistry() {
        this.register("Student", Student.class);
    }

    /** 注册动作*/
    public void register(String alias, Class<?> clazz) {
        TYPE_ALIASES.put(alias, clazz);
    }

    public Map<String, Class<?>> getTYPE_ALIASES() {
        return TYPE_ALIASES;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        NameRegistry registry = new NameRegistry();

        Class<?> clazz = Class.forName("Register.Teacher");

        registry.register(clazz.getSimpleName(), clazz);

        System.out.println( registry.getTYPE_ALIASES());
    }
}
