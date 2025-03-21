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

package io.ballerina.openapi.generators.schema;

import io.ballerina.compiler.syntax.tree.SyntaxTree;
import io.ballerina.openapi.core.generators.common.GeneratorUtils;
import io.ballerina.openapi.core.generators.common.TypeHandler;
import io.ballerina.openapi.core.generators.common.exception.BallerinaOpenApiException;
import io.ballerina.openapi.core.generators.service.ServiceGenerationHandler;
import io.ballerina.openapi.core.generators.service.model.OASServiceMetadata;
import io.swagger.v3.oas.models.OpenAPI;
import org.ballerinalang.formatter.core.FormatterException;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.ballerina.openapi.TestUtils.FILTER;
import static io.ballerina.openapi.generators.common.GeneratorTestUtils.assertGeneratedSyntaxTreeContainsExpectedSyntaxTree;

/**
 * Tests for Special scenarios in schema handling.
 */
public class AdvanceRecordTypeTests {
    private static final Path RES_DIR = Paths.get("src/test/resources/generators/schema").toAbsolutePath();
    SyntaxTree syntaxTree = null;

    // Enable after adding `not` data bind support
    @Test(description = "Generate record for schema has not type")
    public void generateSchemaHasNotType() throws IOException, FormatterException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/scenario10.yaml");
        OpenAPI openAPI = GeneratorUtils.normalizeOpenAPI(definitionPath, true, false);
        TypeHandler.createInstance(openAPI, false);
        ServiceGenerationHandler serviceGenerationHandler = new ServiceGenerationHandler();
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withNullable(false)
                .withFilters(FILTER)
                .build();
        serviceGenerationHandler.generateServiceFiles(oasServiceMetadata);
        syntaxTree = TypeHandler.getInstance().generateTypeSyntaxTree();
        assertGeneratedSyntaxTreeContainsExpectedSyntaxTree("schema/ballerina/schema10.bal", syntaxTree);
        // todo : design a more suitable way to handle not types
    }

    @Test(description = "Generate record for schema has inline record in fields reference")
    public void generateSchemaHasInlineRecord() throws IOException, FormatterException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/scenario11.yaml");
        OpenAPI openAPI = GeneratorUtils.normalizeOpenAPI(definitionPath, true, false);
        TypeHandler.createInstance(openAPI, false);
        ServiceGenerationHandler serviceGenerationHandler = new ServiceGenerationHandler();
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withNullable(false)
                .withFilters(FILTER)
                .build();
        serviceGenerationHandler.generateServiceFiles(oasServiceMetadata);
        syntaxTree = TypeHandler.getInstance().generateTypeSyntaxTree();
        assertGeneratedSyntaxTreeContainsExpectedSyntaxTree("schema/ballerina/schema11.bal", syntaxTree);
    }

    @Test(description = "Generate record for openapi weather api")
    public void generateOpenAPIWeatherAPI() throws IOException, FormatterException, BallerinaOpenApiException {
        Path definitionPath = RES_DIR.resolve("swagger/openapi_weather_api.yaml");
        OpenAPI openAPI = GeneratorUtils.normalizeOpenAPI(definitionPath, true, false);
        TypeHandler.createInstance(openAPI, false);
        ServiceGenerationHandler serviceGenerationHandler = new ServiceGenerationHandler();
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withNullable(false)
                .withFilters(FILTER)
                .build();
        serviceGenerationHandler.generateServiceFiles(oasServiceMetadata);
        syntaxTree = TypeHandler.getInstance().generateTypeSyntaxTree();
        assertGeneratedSyntaxTreeContainsExpectedSyntaxTree("schema/ballerina/openapi_weather_api_schema.bal",
                syntaxTree);
    }

    @Test(description = "Generate record for schema has object type only")
    public void generateForSchemaHasObjectTypeOnly() throws IOException, BallerinaOpenApiException,
            FormatterException {
        Path definitionPath = RES_DIR.resolve("swagger/scenario14.yaml");
        OpenAPI openAPI = GeneratorUtils.normalizeOpenAPI(definitionPath, true, false);
        TypeHandler.createInstance(openAPI, false);
        ServiceGenerationHandler serviceGenerationHandler = new ServiceGenerationHandler();
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withNullable(false)
                .withFilters(FILTER)
                .build();
        serviceGenerationHandler.generateServiceFiles(oasServiceMetadata);
        syntaxTree = TypeHandler.getInstance().generateTypeSyntaxTree();
    }

    @Test(description = "Generate record for schema has allOf types ")
    public void generateTypeForTypeInclusions() throws IOException, BallerinaOpenApiException,
            FormatterException {
        Path definitionPath = RES_DIR.resolve("swagger/inclusion_types.yaml");
        OpenAPI openAPI = GeneratorUtils.normalizeOpenAPI(definitionPath, true, false);
        TypeHandler.createInstance(openAPI, false);
        ServiceGenerationHandler serviceGenerationHandler = new ServiceGenerationHandler();
        OASServiceMetadata oasServiceMetadata = new OASServiceMetadata.Builder()
                .withOpenAPI(openAPI)
                .withNullable(false)
                .withFilters(FILTER)
                .build();
        serviceGenerationHandler.generateServiceFiles(oasServiceMetadata);
        syntaxTree = TypeHandler.getInstance().generateTypeSyntaxTree();
        assertGeneratedSyntaxTreeContainsExpectedSyntaxTree("schema/ballerina/type_inclusion.bal",
                syntaxTree);
    }
}
