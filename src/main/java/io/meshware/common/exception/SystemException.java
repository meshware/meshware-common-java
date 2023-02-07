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
package io.meshware.common.exception;

/**
 * System Exception
 */
public class SystemException extends RuntimeException {

    protected String errorCode;

    protected boolean retry;

    public SystemException() {
    }

    public SystemException(boolean retry) {
        this.retry = retry;
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, boolean retry) {
        super(message);
        this.retry = retry;
    }

    public SystemException(String message, String errorCode, boolean retry) {
        super(message);
        this.errorCode = errorCode;
        this.retry = retry;
    }

    public SystemException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(String message, Throwable cause, boolean retry) {
        super(message, cause);
        this.retry = retry;
    }

    public SystemException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public SystemException(String message, Throwable cause, String errorCode, boolean retry) {
        super(message, cause);
        this.errorCode = errorCode;
        this.retry = retry;
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(Throwable cause, boolean retry) {
        super(cause);
        this.retry = retry;
    }

    public SystemException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public SystemException(Throwable cause, String errorCode, boolean retry) {
        super(cause);
        this.errorCode = errorCode;
        this.retry = retry;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 是否可重试
     *
     * @return bool
     */
    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

}
