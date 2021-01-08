package demo.main;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liaochongwei
 * @date 2021/1/4 9:40
 */
public class TimeConsuming {

    private static final Map<String,Long> TIMES = new HashMap<>();
    public static void mark(String name) {
        TIMES.put(name, System.currentTimeMillis());
    }
    public static void printMS(String name) {
        System.out.println(name + "耗时:" + (System.currentTimeMillis() - TIMES.get(name)) + "ms");
    }

}
