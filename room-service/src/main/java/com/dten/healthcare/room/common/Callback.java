package com.dten.healthcare.room.common;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/9/15
 * @Description:
 */
public abstract class Callback<T> {

    protected final Class<T> resultClass;
    protected final Class<T> failed;

    public Callback(Class<T> resultClass) {
        this(resultClass, null);
    }

    public Callback(Class<T> resultClass, Class<T> failed) {
        this.resultClass = resultClass;
        this.failed = failed;
    }

    public Class<T> getFailed() {
        return failed;
    }
    public Class<T> getResultClass() {
        return resultClass;
    }

    public abstract void onSuccess(T result);

    public abstract void onFail(T result);
}
