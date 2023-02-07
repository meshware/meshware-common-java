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

import io.joyrpc.extension.URL;
import io.meshware.common.Constants;

import java.util.function.BiConsumer;

/**
 * 数据更新事件
 */
public abstract class UpdateEvent<T> extends AbstractEvent {

    /**
     * 事件类型
     */
    protected UpdateType type;

    /**
     * 版本
     */
    protected long version;

    /**
     * 配置
     */
    protected T datum;

    public UpdateEvent(Object source, Object target, UpdateType type, long version, T datum) {
        super(source, target);
        this.type = type;
        this.version = version;
        this.datum = datum;
    }

    public UpdateType getType() {
        return type;
    }

    public long getVersion() {
        return version;
    }

    public T getDatum() {
        return datum;
    }

    /**
     * 更新事件类型
     */
    public enum UpdateType {
        /**
         * 增量更新
         */
        UPDATE {
            @Override
            public void update(URL url, BiConsumer<Boolean, Boolean> consumer) {
                consumer.accept(false, url.getBoolean(Constants.PROTECT_NULL_DATUM_OPTION));
            }
        },
        /**
         * 全量更像
         */
        FULL {
            @Override
            public void update(URL url, BiConsumer<Boolean, Boolean> consumer) {
                consumer.accept(true, url.getBoolean(Constants.PROTECT_NULL_DATUM_OPTION));
            }
        },
        /**
         * 清空
         */
        CLEAR {
            @Override
            public void update(URL url, BiConsumer<Boolean, Boolean> consumer) {
                consumer.accept(true, false);
            }
        };

        /**
         * 事件类型对应的更新操作
         *
         * @param url url
         * @param consumer 参数1：是否为全量数据 参数2：是否需要空保护
         */
        public void update(URL url, BiConsumer<Boolean, Boolean> consumer) {
        }
    }
}
