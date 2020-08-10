package com.github.firelcw.extractor;

import com.github.firelcw.exception.ExtractException;

import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * 提取计划
 * @author liaochongwei
 * @date 2020/8/10 15:38
 */
public class ExtractPlan<T> {
    /**
     * 公共部分类型
     */
    private final Class<T> commonType;
    /**
     * HTTP状态码条件
     */
    private final IntPredicate codeCondition;
    /**
     * 提取条件
     */
    private final Predicate<T> condition;
    /**
     * 提取内容
     */
    private final Function<T,Object> extractor;

    private ExtractPlan(Builder<T> builder) {

        this.commonType = builder.commonType;
        this.codeCondition = builder.codeCondition;
        this.condition = builder.condition;
        if (builder.extractor == null) {
            throw new ExtractException("The extractor is null");
        }
        this.extractor = builder.extractor;
    }

    public Class<T> getCommonType() {
        return commonType;
    }

    public IntPredicate getCodeCondition() {
        return codeCondition;
    }

    public Predicate<T> getCondition() {
        return condition;
    }

    public Function<T,Object> getExtractor() {
        return extractor;
    }

    public static <T> Builder<T> builder(Class<T> commonType) {
        return new Builder<>(commonType);
    }
    public static class Builder<T> {
        /**
         * 公共部分类型
         */
        private final Class<T> commonType;
        /**
         * HTTP状态码条件
         */
        private IntPredicate codeCondition;
        /**
         * 提取条件
         */
        private Predicate<T> condition;
        /**
         * 提取内容
         */
        private Function<T,Object> extractor;

        public Builder(Class<T> commonType) {
            this.commonType = commonType;
        }

        public ExtractPlan<T> build() {
            return new ExtractPlan<>(this);
        }

        public Builder<T> addCodeCondition(IntPredicate codeCondition) {
            this.codeCondition = codeCondition;
            return this;
        }

        public Builder<T> addCondition(Predicate<T> condition) {
            this.condition = condition;
            return this;
        }
        public Builder<T> addExtractor(Function<T,Object> extractor) {
            this.extractor = extractor;
            return this;
        }
    }



}
