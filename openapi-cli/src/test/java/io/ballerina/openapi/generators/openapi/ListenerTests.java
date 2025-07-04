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
package io.ballerina.openapi.generators.openapi;

import io.ballerina.openapi.cmd.OASContractGenerator;
import io.ballerina.openapi.service.mapper.ServersMapperImpl;
import io.ballerina.openapi.service.mapper.diagnostic.OpenAPIMapperDiagnostic;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * This Test class for storing all the endpoint related tests
 * {@link ServersMapperImpl}.
 */
public class ListenerTests {
    private static final Path RES_DIR = Paths.get("src/test/resources/ballerina-to-openapi/").toAbsolutePath();
    private Path tempDir;

    @BeforeMethod
    public void setup() throws IOException {
        this.tempDir = Files.createTempDirectory("bal-to-openapi-test-out-" + System.nanoTime());
    }
    //Listeners
    @Test(description = "Generate OpenAPI spec for single listener")
    public void testListeners01() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario01.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario01.yaml");
    }

    @Test(description = "Generate OpenAPI spec for listener only have port")
    public void testListeners02() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario02.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario02.yaml");
    }

    @Test(description = "Generate OpenAPI spec for multiple listeners")
    public void testListeners03() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario03.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario03.yaml");
    }

    @Test(description = "Generate OpenAPI spec for ExplicitNewExpressionNode listeners")
    public void testListeners04() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario04.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario04.yaml");
    }

    @Test(description = "Generate OpenAPI spec for multiple listeners")
    public void testListeners05() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario05.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario05.yaml");
    }

    @Test(description = "When given ballerina file contain some compilation issue.")
    public void testListeners06() {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario06.bal");
        OASContractGenerator openApiConverter = new OASContractGenerator();
        openApiConverter.generateOAS3DefinitionsAllService(ballerinaFilePath, tempDir, null
                , false);
        List<OpenAPIMapperDiagnostic> errors = openApiConverter.getDiagnostics();
        Assert.assertTrue(errors.isEmpty());
    }

    @Test(description = "Generate OpenAPI spec for http load balancer listeners")
    public void testListeners07() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_http_load_balancer.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/with_check_key_word.yaml");
    }

    @Test(description = "Generate OpenAPI spec for listener with named port arguments")
    public void testListenerWithNamedPort() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario07.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario07.yaml");
    }

    @Test(description = "Generate OpenAPI spec for listener with named port arguments")
    public void testPositionalArgsWithEmptyListenerConfig() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario08.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario08.yaml");
    }

    @Test(description = "Generate OpenAPI spec for listener with positional port arguments")
    public void testListenerWithPositionalPort() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario09.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario09.yaml");
    }

    @Test(description = "Generate OpenAPI spec for listener with positional port arguments")
    public void testListenerWithNamedPortAndHostInConfig() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario10.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario10.yaml");
    }

    @Test(description = "Generate OpenAPI spec for listener with positional port arguments")
    public void testListenerWithNamedPortAndHost() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario11.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario11.yaml");
    }

    @Test(description = "Generate OpenAPI spec for listener with positional port arguments")
    public void testPositionalArgsWithPortAndHostConfig() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario12.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario12.yaml");
    }

    @Test(description = "Generate OpenAPI spec for listener with positional port arguments")
    public void testNamedArgsWithPortAndEmptyConfig() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario13.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario13.yaml");
    }

    @Test(description = "Generate OpenAPI spec for listener with positional port arguments")
    public void testPositionalPortAndNamedConfigWithHost() throws IOException {
        Path ballerinaFilePath = RES_DIR.resolve("listeners/listener_scenario14.bal");
        TestUtils.compareWithGeneratedFile(ballerinaFilePath, "listeners/listener_scenario14.yaml");
    }

    @AfterMethod
    public void cleanUp() {
        TestUtils.deleteDirectory(this.tempDir);
    }

    @AfterTest
    public void clean() {
        System.setErr(null);
        System.setOut(null);
    }

}
