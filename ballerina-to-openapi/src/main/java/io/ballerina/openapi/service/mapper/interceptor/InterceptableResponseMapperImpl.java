/*
 *  Copyright (c) 2024, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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
package io.ballerina.openapi.service.mapper.interceptor;

import io.ballerina.compiler.api.SemanticModel;
import io.ballerina.compiler.syntax.tree.FunctionDefinitionNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NodeList;
import io.ballerina.compiler.syntax.tree.ServiceDeclarationNode;
import io.ballerina.openapi.service.mapper.model.AdditionalData;
import io.ballerina.openapi.service.mapper.model.OperationInventory;
import io.ballerina.openapi.service.mapper.response.ResponseMapper;
import io.ballerina.openapi.service.mapper.response.ResponseMapperImpl;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.stream.StreamSupport;

public class InterceptableResponseMapperImpl implements ResponseMapper {
    private final ApiResponses apiResponses = new ApiResponses();
    private final OperationInventory operationInventory;
    private final SemanticModel semanticModel;

    public InterceptableResponseMapperImpl(ServiceDeclarationNode serviceNode, FunctionDefinitionNode resource,
                                           OperationInventory operationInventory, Components components,
                                           AdditionalData additionalData) {
        this.operationInventory = operationInventory;
        this.semanticModel = additionalData.semanticModel();


        if (isInterceptable(serviceNode)) {
            getInterceptorReturnTypes(semanticModel);
        } else {
            ResponseMapper responseMapperImpl = new ResponseMapperImpl(resource, operationInventory, components,
                    additionalData);
            responseMapperImpl.initializeResponseMapper(resource);
        }
    }

    @Override
    public void setApiResponses() {
        operationInventory.setApiResponses(apiResponses);
    }

    @Override
    public void initializeResponseMapper(FunctionDefinitionNode resourceNode) {}

    private boolean isInterceptable(ServiceDeclarationNode serviceNode) {
        if (isInterceptableServiceType(serviceNode)) {
            return hasCreateInterceptorsFunction(serviceNode.members());
        }
        return false;
    }

    private boolean isInterceptableServiceType(ServiceDeclarationNode serviceNode) {
        return serviceNode.typeDescriptor()
                .map(type -> type.toString().trim().equals("http:InterceptableService"))
                .orElse(false);
    }

    private boolean hasCreateInterceptorsFunction(NodeList<Node> functions) {
        return functions.stream()
                .anyMatch(function -> StreamSupport.stream(((FunctionDefinitionNode) function).children().spliterator(),
                                false).anyMatch(child -> child.toString().equals("createInterceptors")));
    }

    private void getInterceptorReturnTypes(SemanticModel semanticModel) {

    }
}
