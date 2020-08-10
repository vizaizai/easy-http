package com.github.firelcw.hander;

import com.github.firelcw.codec.Decoder;
import com.github.firelcw.exception.ExtractException;
import com.github.firelcw.extractor.ExtractPlan;
import com.github.firelcw.model.HttpResponse;

import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * 提取解码处理
 * @author liaochongwei
 * @date 2020/8/10 18:51
 */
public class ExtractDecodeHandler<T> {

    private ExtractPlan<T> extractPlan;
    private final DecodeHandler decodeHandler;

    public ExtractDecodeHandler(DecodeHandler decodeHandler) {
        this.decodeHandler = decodeHandler;
    }

    @SuppressWarnings("unchecked")
    public Object handle() {
        HttpResponse response = decodeHandler.getResponse();
        Decoder decoder = decodeHandler.getDecoder();

        // 提取计划为空，或返回类型与公共类型相同，则无需提取
        if (extractPlan == null
                || this.decodeHandler.getReturnType().getTypeName().equals(extractPlan.getCommonType().getTypeName())) {
            return this.decodeHandler.handle();
        }
        // 公共对象
        T commonObj = (T)decoder.decode(response, extractPlan.getCommonType());

        // HTTP 状态码
        IntPredicate codeCondition = extractPlan.getCodeCondition();
        if (codeCondition!= null && !codeCondition.test(response.getStatusCode())) {
            throw new ExtractException("The condition for HTTP status code is not meet");
        }
        // 提取条件
        Predicate<T> condition = extractPlan.getCondition();
        if (condition != null && !condition.test(commonObj)) {
            throw new ExtractException("The extracting condition is not meet");
        }

        //提取字段
        Function<T, Object> extractor = extractPlan.getExtractor();
        Object result = extractor.apply(commonObj);


        return null;
    }

    public void setExtractPlan(ExtractPlan<T> extractPlan) {
        this.extractPlan = extractPlan;
    }
}
