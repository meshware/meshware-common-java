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
 * 超时对象
 */
public interface Timeout {

    /**
     * 是否过期了
     *
     * @return 过期标识
     */
    boolean isExpired();

    /**
     * 是否放弃了
     *
     * @return 放弃标识
     */
    boolean isCancelled();

    /**
     * 放弃
     *
     * @return 成功标识
     */
    boolean cancel();

}
