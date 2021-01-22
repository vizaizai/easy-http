package demo.main;

import com.github.vizaizai.model.RetrySettings;

/**
 * @author liaochongwei
 * @date 2021/1/6 14:36
 */
public class MainTest {
    public static void main(String[] args) throws ClassNotFoundException {

        RetrySettings retrySettings1 = new RetrySettings();
        RetrySettings retrySettings2 = new RetrySettings();

        System.out.println(retrySettings1.getClass() == retrySettings2.getClass());
        System.out.println(RetrySettings.class == retrySettings2.getClass());
        Class<?> aClass = Class.forName("com.github.vizaizai.model.RetrySettings");
        System.out.println(aClass == retrySettings2.getClass());
    }
}
