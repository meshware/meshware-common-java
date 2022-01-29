package io.meshware.common.timer;

/**
 * 任务
 */
public interface TimeTask extends Runnable {

    /**
     * Task name
     *
     * @return string
     */
    String getName();

    /**
     * Execute time
     *
     * @return long
     */
    long getTime();

}
