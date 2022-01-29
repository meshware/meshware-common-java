package io.meshware.common.timer;

/**
 * 代理任务
 */
public class DelegateTask implements TimeTask {

    /**
     * 名称
     */
    protected String name;

    /**
     * 时间
     */
    protected long time;

    /**
     * 执行代码
     */
    protected Runnable runnable;

    /**
     * 构造函数
     *
     * @param name     名称
     * @param time     时间
     * @param runnable 执行代码
     */
    public DelegateTask(final String name, final long time, final Runnable runnable) {
        this.name = name;
        this.time = time;
        this.runnable = runnable;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void run() {
        if (runnable != null) {
            runnable.run();
        }
    }

}
