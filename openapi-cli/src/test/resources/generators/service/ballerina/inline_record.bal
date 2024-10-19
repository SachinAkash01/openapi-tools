import ballerina/http;

listener http:Listener ep0 = new (80, config = {host: "petstore.openapi.io"});

service /v1 on ep0 {
    # Creates a new user.
    #
    # + return - OK
    resource function post user(@http:Payload user_body payload) returns inline_response_201 {
    }
}
