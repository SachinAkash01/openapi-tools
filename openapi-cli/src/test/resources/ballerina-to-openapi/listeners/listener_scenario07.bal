import ballerina/http;

listener http:Listener ep0 = new (port = 8080);

service /payloadV on ep0 {
    resource function get pets() returns string {
            return "done";
        }
}
