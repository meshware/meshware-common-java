/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.meshware.common.timer;

import io.meshware.common.concurrent.NamedThreadFactory;
import io.meshware.common.util.Shutdown;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 时间轮调度器
 */
@Slf4j
public class Timer {

    /**
     * 默认定时器
     */
    protected static volatile Timer timer;

    /**
     * 延迟队列
     */
    protected DelayQueue<TimeWheel.Slot> queue;

    /**
     * 底层时间轮
     */
    protected TimeWheel timeWheel;

    /**
     * 过期任务执行线程
     */
    protected ExecutorService workerPool;

    /**
     * 轮询延迟队列获取过期任务线程
     */
    protected ExecutorService bossPool;

    /**
     * 放弃的任务
     */
    protected Queue<Task> cancels = new ConcurrentLinkedQueue<>();

    /**
     * 待分配的任务，防止并发
     */
    protected Queue<Task> flying = new ConcurrentLinkedQueue<>();

    /**
     * 待处理的任务计数
     */
    protected AtomicLong tasks = new AtomicLong(0);

    /**
     * 最大待处理任务
     */
    protected long maxTasks;

    /**
     * 任务执行完毕的消费者
     */
    protected Consumer<Task> afterRun;

    /**
     * 放弃的消费者
     */
    protected Consumer<Task> afterCancel;

    /**
     * 任务执行之前的消费者
     */
    protected Consumer<Task> beforeRun;

    /**
     * 构造函数
     *
     * @param tickTime      每一跳时间
     * @param ticks         时间轮有几条
     * @param workerThreads 工作线程数
     */
    public Timer(final long tickTime, final int ticks, final int workerThreads) {
        this(null, tickTime, ticks, workerThreads, 0);
    }

    /**
     * 构造函数
     *
     * @param name          名称
     * @param tickTime      每一跳时间
     * @param ticks         时间轮有几条
     * @param workerThreads 工作线程数
     */
    public Timer(final String name, final long tickTime, final int ticks, final int workerThreads) {
        this(name, tickTime, ticks, workerThreads, 0);
    }

    /**
     * 构造函数
     *
     * @param name          名称
     * @param tickTime      每一跳时间
     * @param ticks         时间轮有几条
     * @param workerThreads 工作线程数
     * @param maxTasks      最大待处理任务
     */
    public Timer(final String name, final long tickTime, final int ticks, final int workerThreads,
                 final long maxTasks) {
        if (tickTime <= 0) {
            throw new IllegalArgumentException("tickTime must be greater than 0");
        } else if (ticks <= 0) {
            throw new IllegalArgumentException("ticks must be greater than 0");
        } else if (workerThreads <= 0) {
            throw new IllegalArgumentException("workerThreads must be greater than 0");
        }
        this.maxTasks = maxTasks;
        this.afterRun = o -> tasks.decrementAndGet();
        this.afterCancel = this::cancel;
        this.beforeRun = this::supply;
        this.queue = new DelayQueue<>();
        this.timeWheel = new TimeWheel(tickTime, ticks, TimeUtil.now(), queue);
        String prefix = name == null || name.isEmpty() ? "timer" : name;
        this.workerPool = Executors.newFixedThreadPool(workerThreads, new NamedThreadFactory(prefix + "-worker", true));
        this.bossPool = Executors.newFixedThreadPool(1, new NamedThreadFactory(prefix + "-boss", true));
        this.bossPool.submit(() -> {
            while (!Shutdown.isShutdown()) {
                try {
                    // 拉取一跳时间
                    TimeWheel.Slot slot = queue.poll(timeWheel.tickTime, TimeUnit.MILLISECONDS);
                    if (!Shutdown.isShutdown()) {
                        // 处理放弃的任务
                        cancel();
                        // 添加新增的任务，如果当前任务已经过期则立刻执行，否则放入后续的槽中
                        supply();
                        if (slot != null) {
                            // 推进一跳
                            timeWheel.advance(slot.expiration);
                            // 执行任务
                            slot.flush(beforeRun);
                        } else {
                            // 推进一跳
                            timeWheel.advance(timeWheel.now + timeWheel.tickTime);
                        }
                    }
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    break;
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw e;
                }
            }
        });
    }

    /**
     * 获取默认的定时器
     *
     * @return 定时器
     */
    public static Timer timer() {
        if (timer == null) {
            synchronized (Timer.class) {
                if (timer == null) {
                    // Parametric parametric = new
                    // MapParametric<>(GlobalContext.getContext());
                    timer = new Timer("default-timer", 1, 512,
                            Math.min(Runtime.getRuntime().availableProcessors() * 2 + 2, 10));
                }
            }
        }
        return timer;
    }

    /**
     * 放弃任务
     */
    protected void cancel() {
        Task task;
        // 移除放弃的任务
        while ((task = cancels.poll()) != null) {
            // 放弃任务，进入队列之前已经修改了计数器，这里不需要再处理。
            task.remove();
        }
    }

    /**
     * 添加任务
     */
    protected void supply() {
        Task task;
        // 添加任务，1跳最多10万次
        for (int i = 0; i < 100000; i++) {
            task = flying.poll();
            if (task == null) {
                break;
            }
            if (!task.isCancelled()) {
                supply(task);
            }
        }
    }

    /**
     * 添加任务
     *
     * @param task 任务
     */
    protected void supply(final Task task) {
        // 添加失败任务直接执行
        if (!timeWheel.add(task)) {
            workerPool.submit(task);
        }
    }

    /**
     * 添加任务，至少需要一跳
     *
     * @param name     名称
     * @param time     任务执行绝对时间
     * @param runnable 执行任务
     * @return 超时对象
     */
    public Timeout add(final String name, final long time, final Runnable runnable) {
        return runnable == null ? null
                : add(new Task(name, timeWheel.getLeastOneTick(time), runnable, afterRun, afterCancel));
    }

    /**
     * 添加延迟执行的任务，至少需要一跳
     *
     * @param name     名称
     * @param delay    延迟任务执行时间
     * @param runnable 执行任务
     * @return 超时对象
     */
    public Timeout delay(final String name, final long delay, final Runnable runnable) {
        if (runnable == null) {
            return null;
        }
        long time = timeWheel.getLeastOneTick(delay + TimeUtil.now());
        return add(new Task(name, time, runnable, afterRun, afterCancel));
    }

    /**
     * 添加任务，至少需要一条
     *
     * @param task 任务
     * @return 超时对象
     */
    public Timeout add(final TimeTask task) {
        if (task == null) {
            return null;
        }
        long time = timeWheel.getLeastOneTick(task instanceof DelayTask ? TimeUtil.now() + task.getTime() : task.getTime());
        return add(new Task(task.getName(), time, task, afterRun, afterCancel));
    }

    /**
     * 添加任务
     *
     * @param task 任务
     * @return 超时对象
     */
    protected Timeout add(final Task task) {
        if (maxTasks > 0 && tasks.incrementAndGet() > maxTasks) {
            tasks.decrementAndGet();
            throw new RejectedExecutionException("the maximum of pending tasks is " + maxTasks);
        }
        flying.add(task);
        return task;
    }

    /**
     * 放弃任务
     *
     * @param task 任务
     */
    protected void cancel(final Task task) {
        tasks.decrementAndGet();
        cancels.add(task);
    }

    /**
     * 任务
     */
    protected static class Task implements Runnable, Timeout {

        protected static final int INIT = 0;

        protected static final int CANCELLED = 1;

        protected static final int EXPIRED = 2;

        protected static final AtomicIntegerFieldUpdater<Task> STATE_UPDATER = AtomicIntegerFieldUpdater
                .newUpdater(Task.class, "state");

        /**
         * 名称
         */
        protected String name;

        /**
         * 执行的时间
         */
        protected long time;

        /**
         * 是否允许多次添加
         */
        // protected boolean allowMultiple;
        /**
         * 任务
         */
        protected Runnable runnable;

        /**
         * 执行完毕的消费者
         */
        protected Consumer<Task> afterRun;

        /**
         * 放弃的消费者
         */
        protected Consumer<Task> afterCancel;

        /**
         * 时间槽
         */
        protected TimeWheel.Slot slot;

        /**
         * 下一个节点
         */
        protected Task next;

        /**
         * 上一个节点
         */
        protected Task pre;

        /**
         * 状态
         */
        protected volatile int state = INIT;

        /**
         * 构造函数
         *
         * @param name        名称
         * @param time        运行的时间点
         * @param runnable    执行器
         * @param afterRun    运行完消费者
         * @param afterCancel 放弃消费者
         */
        public Task(final String name, final long time, final Runnable runnable, final Consumer<Task> afterRun,
                    final Consumer<Task> afterCancel) {
            this.time = time;
            this.name = name;
            this.runnable = runnable;
            this.afterRun = afterRun;
            this.afterCancel = afterCancel;
            this.slot = null;
            this.next = null;
            this.pre = null;
        }

        protected String getName() {
            return name;
        }

        protected long getTime() {
            return time;
        }

        @Override
        public String toString() {
            return name == null || name.isEmpty() ? super.toString() : name;
        }

        @Override
        public void run() {
            if (STATE_UPDATER.compareAndSet(this, INIT, EXPIRED)) {
                runnable.run();
                if (afterRun != null) {
                    afterRun.accept(this);
                }
            }
        }

        @Override
        public boolean isExpired() {
            return state == EXPIRED;
        }

        @Override
        public boolean isCancelled() {
            return state == CANCELLED;
        }

        @Override
        public boolean cancel() {
            if (STATE_UPDATER.compareAndSet(this, INIT, CANCELLED)) {
                if (afterCancel != null) {
                    afterCancel.accept(this);
                }
                return true;
            }
            return false;
        }

        /**
         * 移除
         */
        void remove() {
            if (slot != null) {
                slot.remove(this);
            }
        }

    }

}
