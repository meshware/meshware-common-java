package io.meshware.common.timer;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 时间轮
 */
public class TimeWheel {

    /**
     * 一跳的时间
     */
    protected long tickTime;

    /**
     * 有几跳
     */
    protected int ticks;

    /**
     * 周期
     */
    protected long duration;

    /**
     * 当前时间，是tickTime的整数倍
     */
    protected long now;

    /**
     * 当前槽的位置
     */
    protected int index;

    /**
     * 延迟队列
     */
    protected DelayQueue<Slot> queue;

    /**
     * 时间槽
     */
    protected Slot[] slots;

    /**
     * 下一层时间轮
     */
    protected TimeWheel next;

    /**
     * 时间轮
     *
     * @param tickTime 每跳的实际
     * @param ticks    几跳
     * @param now      当前时间
     * @param queue    延迟队列
     */
    public TimeWheel(final long tickTime, final int ticks, final long now, final DelayQueue<Slot> queue) {
        this.tickTime = tickTime;
        this.ticks = ticks;
        this.duration = ticks * tickTime;
        this.slots = new Slot[ticks];
        // 当前时间为一跳的整数倍
        this.now = now - (now % tickTime);
        this.queue = queue;
        for (int i = 0; i < ticks; i++) {
            slots[i] = new Slot();
        }
    }

    public long getDuration() {
        return duration;
    }

    /**
     * 创建或者获取下一层时间轮
     *
     * @return time wheel
     */
    protected TimeWheel getNext() {
        if (next == null) {
            next = new TimeWheel(duration, ticks, now, queue);
        }
        return next;
    }

    /**
     * 获取至少一跳的时间
     *
     * @param time time
     * @return 时间点
     */
    public long getLeastOneTick(final long time) {
        long result = TimeUtil.now() + tickTime;
        return Math.max(time, result);
    }

    /**
     * 添加任务到时间轮
     *
     * @param task timer task
     * @return bool
     */
    public boolean add(final Timer.Task task) {
        long time = task.getTime() - now;
        if (time < tickTime) {
            // 过期任务直接执行
            return false;
        } else if (time < duration) {
            // 该任务在一个时间轮里面，则加入到对应的时间槽
            int count = (int) (time / tickTime);
            Slot slot = slots[(count + index) % ticks];
            // 添加到槽里面
            if (slot.add(task, now + count * tickTime) == Slot.HEAD) {
                queue.offer(slot);
            }
            return true;
        } else {
            // 放到下一层的时间轮
            return getNext().add(task);
        }
    }

    /**
     * 推进时间
     *
     * @param timestamp time stamp
     */
    public void advance(final long timestamp) {
        if (timestamp >= now + tickTime) {
            now = timestamp - (timestamp % tickTime);
            index++;
            if (index >= ticks) {
                index = 0;
            }
            if (next != null) {
                // 推进下层时间轮时间
                next.advance(timestamp);
            }
        }
    }

    /**
     * 时间槽
     */
    protected static class Slot implements Delayed {

        public static final int HEAD = 1;

        public static final int TAIL = 2;

        /**
         * 过期时间
         */
        protected long expiration = -1L;

        /**
         * 根节点
         */
        protected Timer.Task root = new Timer.Task("root", -1L, null, null, null);

        /**
         * 构造函数
         */
        public Slot() {
            root.pre = root;
            root.next = root;
        }

        /**
         * 新增任务
         *
         * @param task   任务
         * @param expire 新的过期时间
         * @return 位置
         */
        protected int add(final Timer.Task task, final long expire) {
            task.slot = this;
            Timer.Task tail = root.pre;
            task.next = root;
            task.pre = tail;
            tail.next = task;
            root.pre = task;
            if (expiration == -1L) {
                expiration = expire;
                return HEAD;
            }
            return TAIL;
        }

        /**
         * 移除任务
         *
         * @param task task
         */
        protected void remove(final Timer.Task task) {
            task.next.pre = task.pre;
            task.pre.next = task.next;
            task.slot = null;
            task.next = null;
            task.pre = null;
        }

        /**
         * 当前槽已经过期，执行任务
         *
         * @param consumer 消费者
         */
        protected void flush(final Consumer<Timer.Task> consumer) {
            List<Timer.Task> ts = new LinkedList<>();
            Timer.Task task = root.next;
            while (task != root) {
                remove(task);
                ts.add(task);
                task = root.next;
            }
            expiration = -1L;
            ts.forEach(consumer);
        }

        @Override
        public long getDelay(final TimeUnit unit) {
            long delayMs = expiration - TimeUtil.now();
            return Math.max(0, unit.convert(delayMs, TimeUnit.MILLISECONDS));
        }

        @Override
        public int compareTo(final Delayed o) {
            return o instanceof Slot ? Long.compare(expiration, ((Slot) o).expiration) : 0;
        }

    }

}
