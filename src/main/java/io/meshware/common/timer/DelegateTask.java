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
