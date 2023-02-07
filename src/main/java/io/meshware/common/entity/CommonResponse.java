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

import java.util.HashMap;

/**
 * Common Response
 */
public class CommonResponse extends HashMap<String, Object> {

    private static final String MESSAGE = "message";

    private static final String SUCCESS = "success";

    private static final String DATA = "data";

    private static final String CODE = "code";

    private static final String EMPTY = "";

    public boolean isSuccess() {
        return get(SUCCESS) != null && (Boolean) get(SUCCESS);
    }

    public String getMessage() {
        if (get(MESSAGE) != null) {
            return (String) get(MESSAGE);
        }
        return EMPTY;
    }

    private CommonResponse() {
        super();
    }

    public CommonResponse success() {
        return setContent(true, 1, null);
    }

    public CommonResponse success(String message) {
        return setContent(true, 1, message);
    }

    public CommonResponse success(int code, String message) {
        return setContent(true, code, message);
    }

    public CommonResponse fail(String message) {
        return setContent(false, 0, message);
    }

    public CommonResponse fail(int code, String message) {
        return setContent(false, code, message);
    }

    private CommonResponse setContent(boolean success, int code, String message) {
        this.put(SUCCESS, success);
        this.put(CODE, code);
        this.put(MESSAGE, message);
        return this;
    }

    public CommonResponse setData(Object data) {
        return putData(DATA, data);
    }

    public CommonResponse putData(String key, Object data) {
        this.put(key, data);
        return this;
    }

    public static CommonResponse createResponse() {
        return new CommonResponse();
    }

    public static CommonResponse successResponse(Object data) {
        CommonResponse response = createResponse();
        response.success().setData(data);
        return response;
    }

    public static CommonResponse failResponse(String message) {
        CommonResponse response = createResponse();
        response.fail(message);
        return response;
    }

    public static CommonResponse failResponse(int code, String message) {
        CommonResponse response = createResponse();
        response.fail(code, message);
        return response;
    }

    public enum Code {

        ERROR(0), SUCCESS(1), FLOW_LIMIT(10), DEGRADE(11), PARAM_BLOCK(12), SYSTEM_BLOCK(13), AUTHORITY_BLOCK(14);

        private final int value;

        Code(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

}
