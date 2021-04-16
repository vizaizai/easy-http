package demo.model;

import java.util.List;

/**
 * @author liaochongwei
 * @date 2020/12/22 10:07
 */

public class QueryForm<T> {

    private List<String> ids;
    private T tt;
    public T getTt() {
        return tt;
    }

    public void setTt(T tt) {
        this.tt = tt;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
