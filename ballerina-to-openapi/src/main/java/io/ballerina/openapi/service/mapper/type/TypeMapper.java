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

package io.ballerina.openapi.service.mapper.type;

import io.ballerina.compiler.api.symbols.ArrayTypeSymbol;
import io.ballerina.compiler.api.symbols.ErrorTypeSymbol;
import io.ballerina.compiler.api.symbols.IntersectionTypeSymbol;
import io.ballerina.compiler.api.symbols.MapTypeSymbol;
import io.ballerina.compiler.api.symbols.RecordTypeSymbol;
import io.ballerina.compiler.api.symbols.TableTypeSymbol;
import io.ballerina.compiler.api.symbols.TupleTypeSymbol;
import io.ballerina.compiler.api.symbols.TypeReferenceTypeSymbol;
import io.ballerina.compiler.api.symbols.TypeSymbol;
import io.ballerina.compiler.api.symbols.UnionTypeSymbol;
import io.ballerina.openapi.service.mapper.model.AdditionalData;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Map;
import java.util.Objects;

import static io.ballerina.openapi.service.mapper.utils.MapperCommonUtils.getTypeName;

/**
 * The mapper class to map ballerina types to OAS components.
 *
 * @since 1.9.0
 */
public abstract class TypeMapper {

    public static Schema getTypeSchema(TypeSymbol typeSymbol, Components components,
                                       AdditionalData componentMapperData) {
        return getTypeSchema(typeSymbol, components, componentMapperData, false);
    }

    public static Schema getTypeSchema(TypeSymbol typeSymbol, Components components, AdditionalData componentMapperData,
                                       boolean skipNilType) {
        return switch (typeSymbol.typeKind()) {
            case MAP -> MapTypeMapper.getSchema((MapTypeSymbol) typeSymbol, components, componentMapperData);
            case ARRAY ->
                    ArrayTypeMapper.getSchema((ArrayTypeSymbol) typeSymbol, components, componentMapperData);
            case TYPE_REFERENCE -> ReferenceTypeMapper.getSchema((TypeReferenceTypeSymbol) typeSymbol, components,
                    componentMapperData);
            case RECORD -> RecordTypeMapper.getSchema((RecordTypeSymbol) typeSymbol, components,
                    null, componentMapperData);
            case INTERSECTION ->
                    ReadOnlyTypeMapper.getSchema((IntersectionTypeSymbol) typeSymbol, components, componentMapperData);
            case UNION ->
                    UnionTypeMapper.getSchema((UnionTypeSymbol) typeSymbol, components, componentMapperData,
                            skipNilType);
            case TABLE ->
                    TableTypeMapper.getSchema((TableTypeSymbol) typeSymbol, components, componentMapperData);
            case TUPLE ->
                    TupleTypeMapper.getSchema((TupleTypeSymbol) typeSymbol, components, componentMapperData);
            case ERROR ->
                    ErrorTypeMapper.getSchema((ErrorTypeSymbol) typeSymbol, components, componentMapperData);
            default -> SimpleTypeMapper.getTypeSchema(typeSymbol, componentMapperData);
        };
    }

    protected static void createComponentMapping(TypeReferenceTypeSymbol typeSymbol, Components components,
                                                 AdditionalData componentMapperData) {
        Map<String, Schema> schemas = components.getSchemas();
        if (schemas.containsKey(getTypeName(typeSymbol))) {
            return;
        }
        TypeSymbol referredType = typeSymbol.typeDescriptor();
        AbstractTypeMapper mapper = switch (referredType.typeKind()) {
            case TYPE_REFERENCE -> new ReferenceTypeMapper(typeSymbol, componentMapperData);
            case MAP -> new MapTypeMapper(typeSymbol, componentMapperData);
            case RECORD -> new RecordTypeMapper(typeSymbol, componentMapperData);
            case INTERSECTION -> new ReadOnlyTypeMapper(typeSymbol, componentMapperData);
            case ARRAY -> new ArrayTypeMapper(typeSymbol, componentMapperData);
            case UNION -> new UnionTypeMapper(typeSymbol, componentMapperData);
            case TABLE -> new TableTypeMapper(typeSymbol, componentMapperData);
            case TUPLE -> new TupleTypeMapper(typeSymbol, componentMapperData);
            case ERROR -> new ErrorTypeMapper(typeSymbol, componentMapperData);
            default -> new SimpleTypeMapper(typeSymbol, componentMapperData);
        };
        components.addSchemas(getTypeName(typeSymbol), null);
        mapper.addToComponents(components);
    }

    public static void setDefaultValue(Schema schema, Object defaultValue) {
        if (Objects.isNull(defaultValue)) {
            return;
        }
        if (Objects.nonNull(schema.get$ref())) {
            Schema refSchema = new Schema<>();
            refSchema.set$ref(schema.get$ref());
            schema.set$ref(null);
            schema.addAllOfItem(refSchema);
            schema.setType(null);
        }
        schema.setDefault(defaultValue);
    }
}