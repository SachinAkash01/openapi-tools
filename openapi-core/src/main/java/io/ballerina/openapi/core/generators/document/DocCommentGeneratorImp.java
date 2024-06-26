/*
 *  Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package io.ballerina.openapi.core.generators.document;

import io.ballerina.compiler.syntax.tree.SyntaxTree;
import io.ballerina.openapi.core.generators.common.model.GenSrcFile;
import io.swagger.v3.oas.models.OpenAPI;

public class DocCommentGeneratorImp {
    private final OpenAPI openAPI;
    private SyntaxTree syntaxTree;
    private final GenSrcFile.GenFileType type;
    private final boolean isProxyService;

    public DocCommentGeneratorImp(OpenAPI openAPI, SyntaxTree syntaxTree, GenSrcFile.GenFileType type,
                                  boolean isProxyService) {
        this.openAPI = openAPI;
        this.syntaxTree = syntaxTree;
        this.type = type;
        this.isProxyService = isProxyService;
    }
    public SyntaxTree updateSyntaxTreeWithDocComments() {
        //separate the main sections in syntax tree
        // ex: get a client syntax tree

        //todo this is only integrate to type handler rest of the  service adn client integrate later
        switch (type) {
            case GEN_CLIENT:
                //generate client doc comments
                ClientDocCommentGenerator clientDocCommentGenerator = new ClientDocCommentGenerator(syntaxTree,
                        openAPI, true);
                syntaxTree = clientDocCommentGenerator.updateSyntaxTreeWithDocComments();
                break;
            case GEN_SERVICE:
                //generate service doc comments
                ServiceDocCommentGenerator serviceDocCommentGenerator = new ServiceDocCommentGenerator(syntaxTree,
                        openAPI, isProxyService);
                syntaxTree = serviceDocCommentGenerator.updateSyntaxTreeWithDocComments();
                break;
            case GEN_SERVICE_TYPE:
                //generate service type doc comments
                break;
            case GEN_TYPE:
                TypesDocCommentGenerator typesDocCommentGenerator = new TypesDocCommentGenerator(syntaxTree, openAPI);
                syntaxTree = typesDocCommentGenerator.updateSyntaxTreeWithDocComments();
                break;
            case TEST_SRC:
                //generate test src doc comments
                break;
            case CONFIG_SRC:
                //generate config src doc comments
                break;
            case UTIL_SRC:
                //generate util src doc comments
                break;
            case CACHE_SRC:
                //generate cache src doc comments
                break;
        }
        return syntaxTree;
    }
}
