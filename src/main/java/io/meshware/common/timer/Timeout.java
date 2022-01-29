package io.meshware.common.timer;

/**
 * 超时对象
 */
public interface Timeout {

    /**
     * 是否过期了
     *
     * @return 过期标识
     */
    boolean isExpired();

    /**
     * 是否放弃了
     *
     * @return 放弃标识
     */
    boolean isCancelled();

    /**
     * 放弃
     *
     * @return 成功标识
     */
    boolean cancel();

}
