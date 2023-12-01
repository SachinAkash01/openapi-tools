/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
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


package io.ballerina.openapi.service.mapper;

import io.ballerina.compiler.api.SemanticModel;
import io.ballerina.compiler.syntax.tree.FunctionDefinitionNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NodeList;
import io.ballerina.compiler.syntax.tree.ServiceDeclarationNode;
import io.ballerina.compiler.syntax.tree.SyntaxKind;
import io.ballerina.openapi.service.mapper.constraint.ConstraintMapper;
import io.ballerina.openapi.service.mapper.diagnostic.OpenAPIMapperDiagnostic;
import io.ballerina.openapi.service.mapper.model.ModuleMemberVisitor;
import io.ballerina.openapi.service.mapper.type.ComponentMapper;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenAPIServiceMapper provides functionality for reading and writing OpenApi, either to and from ballerina service, or
 * to, as well as related functionality for performing conversions between openapi and ballerina.
 *
 * @since 2.0.0
 */
public class OpenAPIServiceMapper {
    private final SemanticModel semanticModel;
    private final List<OpenAPIMapperDiagnostic> errors = new ArrayList<>();
    private final ModuleMemberVisitor moduleMemberVisitor;

    public List<OpenAPIMapperDiagnostic> getErrors() {
        return errors;
    }

    /**
     * Initializes a service parser for OpenApi.
     */
    public OpenAPIServiceMapper(SemanticModel semanticModel, ModuleMemberVisitor moduleMemberVisitor) {
        // Default object mapper is JSON mapper available in openApi utils.
        this.semanticModel = semanticModel;
        this.moduleMemberVisitor = moduleMemberVisitor;
    }

    /**
     * This method will convert ballerina @Service to openApi @OpenApi object.
     *
     * @param service   - Ballerina @Service object to be map to openApi definition
     * @param openapi   - OpenApi model to populate
     * @return OpenApi object which represent current service.
     */
    public OpenAPI convertServiceToOpenAPI(ServiceDeclarationNode service, OpenAPI openapi) {
        NodeList<Node> functions = service.members();
        List<FunctionDefinitionNode> resources = new ArrayList<>();
        for (Node function: functions) {
            SyntaxKind kind = function.kind();
            if (kind.equals(SyntaxKind.RESOURCE_ACCESSOR_DEFINITION)) {
                resources.add((FunctionDefinitionNode) function);
            }
        }
        ComponentMapper componentMapper = new ComponentMapper(openapi, semanticModel, moduleMemberVisitor, errors);
        OpenAPIResourceMapper resourceMapper = new OpenAPIResourceMapper(openapi, resources, semanticModel,
                moduleMemberVisitor, errors, componentMapper);
        resourceMapper.addMapping();
        ConstraintMapper constraintMapper = new ConstraintMapper(openapi, moduleMemberVisitor, errors);
        constraintMapper.addMapping();
        return openapi;
    }
}