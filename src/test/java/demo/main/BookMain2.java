package demo.main;


import com.github.firelcw.util.TypeUtils;

import java.util.Collections;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:43
 */
public class BookMain2 {
    public static void main(String[] args) {

        System.out.println(TypeUtils.isSimple(String[].class.getTypeName()));
        System.out.println(TypeUtils.isSimple(char.class.getTypeName()));
        System.out.println(TypeUtils.isSimple(Character.class.getTypeName()));

        Collections.emptyList().add("1");
    }

}
