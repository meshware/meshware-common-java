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

/**
 * 抽象的事件
 */
public abstract class AbstractEvent implements Event, Recipient {
    //事件来源
    protected Object source;
    //目标对象
    protected Object target;

    public AbstractEvent(final Object source, final Object target) {
        this.source = source;
        this.target = target;
    }

    public Object getSource() {
        return source;
    }

    @Override
    public Object getTarget() {
        return target;
    }
}
