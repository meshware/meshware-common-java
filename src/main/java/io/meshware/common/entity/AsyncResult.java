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
package io.meshware.common.entity;


import io.meshware.common.event.Event;

public class AsyncResult<T> implements Event {

    protected T result;

    protected boolean success;

    protected Throwable throwable;

    public AsyncResult() {
        this.success = true;
    }

    public AsyncResult(boolean success) {
        this.success = success;
    }

    public AsyncResult(T result) {
        this.result = result;
        this.success = true;
    }

    public AsyncResult(Throwable throwable) {
        this.success = false;
        this.throwable = throwable;
    }

    public AsyncResult(T result, Throwable throwable) {
        this.success = false;
        this.result = result;
        this.throwable = throwable;
    }

    public AsyncResult(AsyncResult source, T result) {
        this.success = source.success;
        this.throwable = source.throwable;
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
