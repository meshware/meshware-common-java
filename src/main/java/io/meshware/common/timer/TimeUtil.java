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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class TimeUtil {

    protected static final TimeUtil instance = new TimeUtil();

    // 精度(毫秒)
    protected long precision;

    // 当前时间
    protected volatile long now;

    // 调度任务
    protected ScheduledExecutorService scheduler;

    public static TimeUtil getInstance() {
        return instance;
    }

    public TimeUtil() {
        this(1L);
    }

    public TimeUtil(long precision) {
        this.precision = precision;
        now = System.currentTimeMillis();
        scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "SystemClock");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> {
            now = System.currentTimeMillis();
        }, precision, precision, TimeUnit.MILLISECONDS);
    }

    public long getTime() {
        return now;
    }

    public long precision() {
        return precision;
    }

    /**
     * 获取当前时钟
     *
     * @return 当前时钟
     */
    public static long now() {
        return instance.getTime();
    }

    public static long microTime() {
        long microTime = now() * 1000;
        long nanoTime = System.nanoTime();
        return microTime + (nanoTime % 1000000) / 1000;
    }
}
