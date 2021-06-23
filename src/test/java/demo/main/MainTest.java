package demo.main;

import java.util.UUID;

/**
 * @author liaochongwei
 * @date 2021/1/6 14:36
 */
public class MainTest {
    public static void main(String[] args) throws ClassNotFoundException {

        System.out.println(UUID.randomUUID().toString().replace("-",""));
    }
}
