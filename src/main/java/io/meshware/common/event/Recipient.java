package io.meshware.common.event;

/**
 * 指定监听器
 */
public interface Recipient {

    /**
     * 返回接收该事件的监听器
     *
     * @return obj
     */
    Object getTarget();
}
