package Register;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lanyage on 2018/5/6.
 */
public class NameRegistry {
    private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<>();

    public NameRegistry() {
        this.register("Student", Student.class);
    }
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
