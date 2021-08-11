package com.github.vizaizai.parser;

import com.github.vizaizai.annotation.Body;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 参数解析
 * @author liaochongwei
 * @date 2021/2/4 14:33
 */
public class ArgsParser {
    private final List<Arg> args;

    public static ArgsParser doParse(List<Arg> args) {
        return new ArgsParser(args);
    }
    private ArgsParser(List<Arg> args) {
       this.args = args;
       this.check();
    }

    private void check() {
        // 1. 只能包含一个@Body
        int has1 = 0;
        for (Arg arg : args) {
            arg.parse();
            if (Body.TYPE.equals(arg.getType())) {
                has1 ++;
            }
        }
        if (has1 > 1) {
            throw new IllegalArgumentException("@Body must be unique");
        }
    }

    public boolean isEmpty() {
        return this.args.isEmpty();
    }

    public List<Arg> getArgs(String type) {
        return args.stream().filter(e-> e.getType().equals(type)).collect(Collectors.toList());
    }

    public int getCount(String type) {
        return (int) args.stream().filter(e->e.getType().equals(type)).count();
    }
}
