/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.openapi.service.mapper.hateoas;

import io.ballerina.compiler.api.SemanticModel;
import io.ballerina.compiler.api.symbols.ServiceDeclarationSymbol;
import io.ballerina.compiler.api.symbols.Symbol;
import io.ballerina.compiler.syntax.tree.AnnotationNode;
import io.ballerina.compiler.syntax.tree.FunctionDefinitionNode;
import io.ballerina.compiler.syntax.tree.MappingConstructorExpressionNode;
import io.ballerina.compiler.syntax.tree.MetadataNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NodeList;
import io.ballerina.compiler.syntax.tree.NodeVisitor;
import io.ballerina.compiler.syntax.tree.ServiceDeclarationNode;
import io.ballerina.compiler.syntax.tree.SpecificFieldNode;
import io.ballerina.compiler.syntax.tree.SyntaxKind;
import io.ballerina.openapi.service.mapper.utils.MapperCommonUtils;

import java.util.Optional;

import static io.ballerina.openapi.service.mapper.hateoas.ContextHolder.getHateoasContextHolder;

public class HateoasMetadataVisitor extends NodeVisitor {
    private final SemanticModel semanticModel;

    public HateoasMetadataVisitor(SemanticModel semanticModel) {
        this.semanticModel = semanticModel;
    }

    @Override
    public void visit(ServiceDeclarationNode serviceNode) {
        boolean isHttpService = MapperCommonUtils.isHttpService(serviceNode, semanticModel);
        if (!isHttpService) {
            return;
        }
        Optional<Symbol> serviceDeclarationOpt = semanticModel.symbol(serviceNode);
        if (serviceDeclarationOpt.isEmpty()) {
            return;
        }
        ServiceDeclarationSymbol serviceSymbol = (ServiceDeclarationSymbol) serviceDeclarationOpt.get();
        int serviceId = serviceSymbol.hashCode();
        for (Node child : serviceNode.children()) {
            if (SyntaxKind.RESOURCE_ACCESSOR_DEFINITION.equals(child.kind())) {
                FunctionDefinitionNode resourceFunction = (FunctionDefinitionNode) child;
                String resourceMethod = resourceFunction.functionName().text();
                String operationId = generateOperationId(resourceFunction);
                Optional<String> resourceName = getResourceConfigAnnotation(resourceFunction)
                        .flatMap(resourceConfig -> getValueForAnnotationFields(resourceConfig, "name"));
                if (resourceName.isEmpty()) {
                    return;
                }
                Resource hateoasResource = new Resource(resourceMethod, operationId);
                getHateoasContextHolder().updateHateoasResource(serviceId, resourceName.get(), hateoasResource);
            }
        }
    }

    private String generateOperationId(FunctionDefinitionNode resourceFunction) {
        String relativePath = MapperCommonUtils.generateRelativePath(resourceFunction);
        String cleanResourcePath = MapperCommonUtils.unescapeIdentifier(relativePath);
        String resName = (resourceFunction.functionName().text() + "_" +
                cleanResourcePath).replaceAll("\\{///\\}", "_");

        if (cleanResourcePath.equals("/")) {
            resName = resourceFunction.functionName().text();
        }
        return MapperCommonUtils.getOperationId(resName);
    }

    private Optional<AnnotationNode> getResourceConfigAnnotation(FunctionDefinitionNode resourceFunction) {
        Optional<MetadataNode> metadata = resourceFunction.metadata();
        if (metadata.isEmpty()) {
            return Optional.empty();
        }
        MetadataNode metaData = metadata.get();
        NodeList<AnnotationNode> annotations = metaData.annotations();
        return annotations.stream()
                .filter(ann -> "http:ResourceConfig".equals(ann.annotReference().toString().trim()))
                .findFirst();
    }

    private Optional<String> getValueForAnnotationFields(AnnotationNode resourceConfigAnnotation, String fieldName) {
        return resourceConfigAnnotation
                .annotValue()
                .map(MappingConstructorExpressionNode::fields)
                .flatMap(fields ->
                        fields.stream()
                                .filter(fld -> fld instanceof SpecificFieldNode)
                                .map(fld -> (SpecificFieldNode) fld)
                                .filter(fld -> fieldName.equals(fld.fieldName().toString().trim()))
                                .findFirst()
                ).flatMap(SpecificFieldNode::valueExpr)
                .map(en -> en.toString().trim());
    }
}