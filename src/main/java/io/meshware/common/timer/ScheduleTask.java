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

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * ScheduleTask
 *
 * @author Zhiguo.Chen
 * @since 20211019
 */
@Slf4j
public abstract class ScheduleTask implements TimeTask {

    private long time;
    private final Timer currentTimer;
    private volatile boolean running = true;

    public ScheduleTask(Timer currentTimer) {
        this.currentTimer = currentTimer;
    }

    /**
     * 执行时间
     *
     * @return long
     */
    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void run() {
        if (running) {
            try {
                CompletableFuture<Void> completableFuture = execute();
                if (!isAsync()) {
                    completableFuture.get();
                }
            } catch (Exception e) {
                log.error("Task execute error! message={}", e.getMessage(), e);
            }
            time = TimeUtil.now() + getIntervalInMs();
            currentTimer.add(this);
        }
    }

    public void stop() {
        running = false;
    }

    /**
     * Execute
     *
     * @return future
     */
    public abstract CompletableFuture<Void> execute();

    /**
     * Return interval time in millisecond
     *
     * @return interval time
     */
    public abstract int getIntervalInMs();

    /**
     * Async or sync
     *
     * @return bool
     */
    public abstract boolean isAsync();
}
