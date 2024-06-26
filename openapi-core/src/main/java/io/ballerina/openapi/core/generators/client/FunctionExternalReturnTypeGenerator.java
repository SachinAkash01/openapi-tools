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
package io.ballerina.openapi.core.generators.client;

import io.ballerina.compiler.syntax.tree.ReturnTypeDescriptorNode;
import io.ballerina.compiler.syntax.tree.TypeDescriptorNode;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;

import java.util.Optional;

import static io.ballerina.compiler.syntax.tree.AbstractNodeFactory.createEmptyNodeList;
import static io.ballerina.compiler.syntax.tree.AbstractNodeFactory.createIdentifierToken;
import static io.ballerina.compiler.syntax.tree.AbstractNodeFactory.createToken;
import static io.ballerina.compiler.syntax.tree.NodeFactory.createReturnTypeDescriptorNode;
import static io.ballerina.compiler.syntax.tree.NodeFactory.createSimpleNameReferenceNode;
import static io.ballerina.compiler.syntax.tree.NodeFactory.createUnionTypeDescriptorNode;
import static io.ballerina.compiler.syntax.tree.SyntaxKind.PIPE_TOKEN;
import static io.ballerina.compiler.syntax.tree.SyntaxKind.RETURNS_KEYWORD;

/**
 * This class is used to generate the return type of the client external function.
 *
 * @since 1.9.0
 */
public class FunctionExternalReturnTypeGenerator extends FunctionStatusCodeReturnTypeGenerator {
    public FunctionExternalReturnTypeGenerator(Operation operation, OpenAPI openAPI, String httpMethod, String path,
                                               BallerinaUtilGenerator ballerinaUtilGenerator) {
        super(operation, openAPI, httpMethod, path, ballerinaUtilGenerator);
    }

    @Override
    public Optional<ReturnTypeDescriptorNode> getReturnType() {
        TypeDescriptorNode targetTypeOrError = createUnionTypeDescriptorNode(
                createSimpleNameReferenceNode(createIdentifierToken("targetType")),
                createToken(PIPE_TOKEN),
                createSimpleNameReferenceNode(createIdentifierToken("error")));
        return Optional.of(createReturnTypeDescriptorNode(createToken(RETURNS_KEYWORD), createEmptyNodeList(),
                targetTypeOrError));
    }
}
