package com.github.firelcw.extractor;

import com.github.firelcw.codec.Decoder;
import com.github.firelcw.exception.ExtractException;
import com.github.firelcw.model.HttpResponse;

import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 提取计划
 * @author liaochongwei
 * @date 2020/8/10 15:38
 */
public class ExtractPlan1 {
    /**
     * 解码器
     */
    private Decoder decoder;
    /**
     * 响应器
     */
    private HttpResponse response;
    /**
     * 待提取类型类型
     */
    private Type extractType;

    /**
     * 公共部分
     * @param commonType 公共类型
     */
    @SuppressWarnings("unchecked")
    public <T> CommonPart<T> commonPart(Class<T> commonType) {
        // 待提取类型等于公共类型，则不要用提取
        if (commonType.getTypeName().equals(this.extractType.getTypeName())) {
            return new CommonPart<>(false);
        }
        CommonPart<T> commonPart = new CommonPart<>(true);
        commonPart.statusCode = response.getStatusCode();
        commonPart.common = (T)this.decoder.decode(response,commonType);

        return commonPart;
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public void setDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public Type getExtractType() {
        return extractType;
    }

    public void setExtractType(Type extractType) {
        this.extractType = extractType;
    }

    public static class CommonPart<T> {
        /**
         * 是否需要提取
         */
        private final boolean isNeedExtract;
        /**
         * http状态码
         */
        private Integer statusCode;
        /**
         * 公共部分
         */
        private T common;
        /**
         * HTTP状态码条件
         */
        private Predicate<Integer> codeCondition;
        /**
         * 提取条件
         */
        private Predicate<T> condition;
        /**
         * 提取内容
         */
        private Function<T,Object> extractor;

        public CommonPart(boolean isNeedExtract) {
            this.isNeedExtract = isNeedExtract;
        }

        public CommonPart<T> addCodeCondition(Predicate<Integer> codeCondition) {
            this.codeCondition = codeCondition;
            return this;
        }

        public CommonPart<T> addCondition(Predicate<T> condition) {
            this.condition = condition;
            return this;
        }
        public CommonPart<T> addExtractor(Function<T,Object> extractor) {
            this.extractor = extractor;
            return this;
        }

        /**
         * http状态码是否符合
         * @return boolean
         */
        public boolean isHttpOk() {
            if (this.codeCondition == null) {
                return this.statusCode >= 200 && this.statusCode <= 300;
            }
            return this.codeCondition.test(this.statusCode);
        }
        /**
         * 提取条件是否符合
         * @return boolean
         */
        public boolean isOk() {
            if (this.condition == null) {
                throw new ExtractException("The extracting condition is null");
            }
            return this.condition.test(this.common);
        }

        /**
         * 获取提取内容
         * @return Object
         */
        public Object getExtractedData() {
            if (this.extractor == null) {
                throw new ExtractException("The extractor is null");
            }
            return this.extractor.apply(this.common);
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public T getCommon() {
            return common;
        }

        public void setCommon(T common) {
            this.common = common;
        }

        public Predicate<Integer> getCodeCondition() {
            return codeCondition;
        }

        public Predicate<T> getCondition() {
            return condition;
        }

        public Function<T, Object> getExtractor() {
            return extractor;
        }

        public boolean isNeedExtract() {
            return isNeedExtract;
        }
    }
}
