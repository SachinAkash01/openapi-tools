/*
 *  Copyright (c) 2024, WSO2 LLC. (http://www.wso2.org).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package io.ballerina.openapi.client;

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BObject;

/**
 * This class contains the generated client's native code.
 *
 * @since 1.9.0
 */
public class GeneratedClient {

    public static Object invokeResource(Environment env, BObject client, BArray pathParams, BArray params) {
        String functionName = env.getFunctionName();
        try {
            String methodName = ClientUtil.getResourceImplFunctionName(functionName, client);
            return invokeClientMethod(env, client, pathParams, params, methodName);
        } catch (RuntimeException e) {
            return ClientUtil.createHttpError("error in invoking client resource method: " + e.getMessage(), null);
        }
    }

    public static Object invokeResourceWithoutPath(Environment env, BObject client, BArray params) {
        String functionName = env.getFunctionName();
        try {
            String methodName = ClientUtil.getResourceImplFunctionName(functionName, client);
            return invokeClientMethod(env, client, params, methodName);
        } catch (RuntimeException e) {
            return ClientUtil.createHttpError("error in invoking client resource method: " + e.getMessage(), null);
        }
    }

    public static Object invoke(Environment env, BObject client, BArray params) {
        String functionName = env.getFunctionName();
        try {
            String methodName = ClientUtil.getRemoteImplFunctionName(functionName, client);
            return invokeClientMethod(env, client, params, methodName);
        } catch (RuntimeException e) {
            return ClientUtil.createHttpError("error in invoking client remote method: " + e.getMessage(), null);
        }
    }

    private static Object invokeClientMethod(Environment env, BObject client, BArray path, BArray params,
                                             String methodName) {
        int pathLength = (int) path.getLength();
        int paramLength = (int) params.getLength();

        Object[] paramFeed = new Object[pathLength + paramLength];
        for (int i = 0; i < pathLength; i++) {
            paramFeed[i] = path.get(i);
        }
        for (int i = 0; i < paramLength; i++) {
            paramFeed[pathLength + i] = params.get(i);
        }

        return invokeClientMethod(env, client, methodName, paramFeed);
    }

    private static Object invokeClientMethod(Environment env, BObject client, BArray params,
                                             String methodName) {
        int paramLength = (int) params.getLength();

        Object[] paramFeed = new Object[paramLength];
        for (int i = 0; i < paramLength; i++) {
            paramFeed[i] = params.get(i);
        }

        return invokeClientMethod(env, client, methodName, paramFeed);
    }

    private static Object invokeClientMethod(Environment env, BObject client, String methodName, Object[] paramFeed) {
        return env.yieldAndRun(() -> {
            try {
                return env.getRuntime().callMethod(client, methodName, null, paramFeed);
            } catch (BError bError) {
                return ClientUtil.createHttpError("client method invocation failed: " +
                        bError.getErrorMessage(), bError);
            }
        });
    }
}
