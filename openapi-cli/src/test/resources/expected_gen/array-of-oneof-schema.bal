// AUTO-GENERATED FILE.
// This file is auto-generated by the Ballerina OpenAPI tool.

import ballerina/http;

listener http:Listener ep0 = new (8080, config = {host: "localhost"});

service /socialMedia/v1 on ep0 {
    # + return - Ok
    resource function get test() returns (string|int|record {string name?; int age?;})[] {
    }
}