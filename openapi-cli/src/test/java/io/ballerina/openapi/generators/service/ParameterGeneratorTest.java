/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.openapi.generators.service;

import io.ballerina.compiler.syntax.tree.SyntaxTree;
import io.ballerina.openapi.TestUtils;
import io.ballerina.openapi.core.generators.common.GeneratorUtils;
import io.ballerina.openapi.core.generators.common.TypeHandler;
import io.ballerina.openapi.core.generators.common.exception.BallerinaOpenApiException;
import io.ballerina.openapi.core.generators.common.model.Filter;
import io.ballerina.openapi.core.generators.service.ServiceDeclarationGenerator;
import io.ballerina.openapi.core.generators.service.model.OASServiceMetadata;
import io.ballerina.tools.diagnostics.Diagnostic;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * All the tests related to the Ballerina Service Generator.
 */
public class ParameterGeneratorTest {

    private static final Path RES_DIR = Paths.get("src/test/resources/generators/service/").toAbsolutePath();
    List<String> list1 = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    Filter filter = new Filter(list1, list2);
    SyntaxTree syntaxTree;

    @Test(description = "Generate serviceDeclaration")
    public void generateService() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/petstore_service.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("service_gen.bal", syntaxTree);
    }

    @Test(description = "Generate service with path having special characters")
    public void generateServiceWithPathSpecialCharacters() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/petstore_service_with_special_characters.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("service_gen_special_characters.bal",
                syntaxTree);
    }

    @Test(description = "Generate functionDefinitionNode for multiple operations")
    public void generateMultipleOperations() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/multiOperations.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("multi_operations.bal", syntaxTree);
    }

    @Test(description = "Generate functionDefinitionNode for multiple paths")
    public void generateMultiplePath() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/multiPaths.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("multi_paths.bal", syntaxTree);
    }

    //Scenario 01 - Path parameters.
    @Test(description = "Generate functionDefinitionNode for Path parameters")
    public void generatePathParameter() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/multiPathParam.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("path_parameters.bal", syntaxTree);
    }

    //Scenario 02 - Path parameters.
    @Test(description = "Generate functionDefinitionNode for only Path parameters")
    public void generatePathParameter02() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/multiPathParam02.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("path_parameters02.bal", syntaxTree);
    }

    @Test(description = "Tests when path parameter(s) having a keyword as the parameter name")
    public void generatePathParameter03() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/multiPathParam03.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("path_parameters03.bal", syntaxTree);
    }

    //Scenario 02 - Query parameters.
    @Test(description = "Generate functionDefinitionNode for Query parameters")
    public void generateQueryParameter() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/multiQueryParam.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("query_parameters.bal", syntaxTree);
        TestUtils.compareDiagnosticWarnings(ballerinaServiceGenerator.getDiagnostics(),
                "Query parameters with nested array types are not supported in Ballerina.");
    }

    @Test(description = "Generate functionDefinitionNode for paramter for content instead of schema")
    public void generateParameterHasContent() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/parameterTypehasContent.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("param_type_with_content.bal", syntaxTree);
        TestUtils.compareDiagnosticWarnings(ballerinaServiceGenerator.getDiagnostics(),
                "Type 'json' is not a valid query parameter type in Ballerina. The supported " +
                        "types are string, int, float, boolean, decimal, array types of the " +
                        "aforementioned types and map<json>.");
    }

    @Test(description = "Tests when query parameter(s) having a keyword as the parameter name")
    public void withKeyWords() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/keywords.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("keywords.bal", syntaxTree);
    }

    @Test(description = "Tests for all the enum scenarios in resource function parameter generation:" +
            "Use case 01 : Enum in query parameter" +
            "Use case 02 : Enums in path parameter" +
            "Use case 03 : Enum in header parameter" +
            "Use case 04 : Enum in reusable parameter" +
            "Use case 05 : Enum in parameter with referenced schema")
    public void generateParametersWithEnums() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/parameters_with_enum.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("parameters_with_enum.bal", syntaxTree);
    }

    @Test(description = "Tests for all the nullable enum scenarios in resource function parameter generation:" +
            "Use case 01 : Nullable enum in query parameter" +
            "Use case 02 : Nullable enum in header parameter" +
            "Use case 03 : Nullable enum in reusable parameter" +
            "Use case 04 : Nullable enum in parameter with referenced schema")
    public void generateParametersWithNullableEnums() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/parameters_with_nullable_enums.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree(
                "parameters_with_nullable_enums.bal", syntaxTree);
    }

    @Test(description = "Test unsupported nullable path parameter with enums")
    public void testNullablePathParamWithEnum() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/path_param_nullable.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        List<Diagnostic> diagnostics = ballerinaServiceGenerator.getDiagnostics();
        TestUtils.compareDiagnosticWarnings(diagnostics, "Path parameter value cannot be null.");
    }

    @Test(description = "Tests int32, int64, and invalid integer path parameters")
    public void testsIntegerPathParameters() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/intPathParam.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree("intPathParam.bal", syntaxTree);
    }

    @Test(description = "Tests for referenced parameters in OpenAPI 3.1 version specs")
    public void testsRefParamsInOpenAPIV31() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/parameter_with_ref_v31.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree(
                "parameter_with_ref_v31.bal", syntaxTree);
    }

    @Test(description = "Tests for path segments has parameters with extension")
    public void testsForPathSegmentHasExtensionType() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/multiPathParamWithExtensionType.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree(
                "multiPathParamWithExtensionType.bal", syntaxTree);
    }

    @Test(description = "Tests for path parameters in the common section for v3.1.0")
    public void testsForPathParametersInCommonSectionForV310() throws IOException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/commonPathParamsV310.yaml");
        OpenAPI openAPI = GeneratorUtils.getOpenAPIFromOpenAPIV3Parser(definitionPath);
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withFilters(filter)
                .build();
        TypeHandler.createInstance(openAPI, false);
        ServiceDeclarationGenerator ballerinaServiceGenerator = new ServiceDeclarationGenerator(oasServiceMetadata);
        syntaxTree = ballerinaServiceGenerator.generateSyntaxTree();
        CommonTestFunctions.compareGeneratedSyntaxTreewithExpectedSyntaxTree(
                "commonPathParamsV310.bal", syntaxTree);
    }
}
