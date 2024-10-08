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

package io.ballerina.openapi.core.generators.client.parameter;

import io.ballerina.compiler.syntax.tree.ParameterNode;
import io.ballerina.openapi.core.generators.client.diagnostic.ClientDiagnostic;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static io.ballerina.openapi.core.generators.common.GeneratorConstants.X_PARAM_TYPE;

public interface ParameterGenerator {
    //type handler attribute
    Optional<ParameterNode> generateParameterNode();
    List<ClientDiagnostic> getDiagnostics();

    default Schema getSchemaWithDetails(Parameter parameter) {
        Schema schema = parameter.getSchema();
        if (Objects.isNull(schema)) {
            return null;
        }
        Optional.ofNullable(parameter.getDescription()).ifPresent(schema::setDescription);
        Optional.ofNullable(parameter.getDeprecated()).ifPresent(schema::setDeprecated);
        Map<String, Object> extensions = parameter.getExtensions();
        if (Objects.isNull(extensions)) {
            extensions = new HashMap<>();
        }
        extensions.put(X_PARAM_TYPE, parameter.getIn());
        schema.setExtensions(extensions);
        return schema;
    }
}
