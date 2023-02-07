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
package io.meshware.common.event;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/**
 * 消息发布者
 */
public interface Publisher<E extends Event> extends Closeable {

    /**
     * 启动
     */
    void start();

    /**
     * 关闭
     */
    void close();

    /**
     * 添加一个handler
     *
     * @param handler handler
     * @return bool
     */
    boolean addHandler(EventHandler<E> handler);

    /**
     * 添加处理器
     *
     * @param handlers handlers
     * @param <M>      M
     */
    default <M extends EventHandler<E>> void addHandler(final Iterable<M> handlers) {
        if (handlers != null) {
            handlers.forEach(o -> addHandler(o));
        }
    }

    /**
     * 移除一个handler
     *
     * @param handler handler
     * @return bool
     */
    boolean removeHandler(EventHandler<E> handler);

    /**
     * 返回处理器的数量
     *
     * @return int
     */
    int size();

    /**
     * 添加一个事件
     *
     * @param event 事件
     * @return bool
     */
    boolean offer(E event);

    /**
     * 添加一个事件
     *
     * @param event    事件
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     * @return bool
     */
    boolean offer(E event, long timeout, TimeUnit timeUnit);
}
