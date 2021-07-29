import ballerina/http;

# + clientEp - Connector http endpoint
public client class Client {
    http:Client clientEp;
    # Client initialization.
    #
    # + clientConfig - Client configuration details
    # + serviceUrl - Connector server URL
    # + return - Returns error at failure of client initialization
    public isolated function init(http:ClientConfiguration clientConfig =  {}, string serviceUrl = "localhost:9090/payloadV") returns error? {
        http:Client httpEp = check new (serviceUrl, clientConfig);
        self.clientEp = httpEp;
    }
    #
    # + 'version - Version Id
    # + versionName - Version Name
    # + return - Ok
    remote isolated function operationId04(int 'version, string versionName) returns string|error {
        string  path = string `/v1/${'version}/v2/${'versionName}`;
        string response = check self.clientEp-> get(path, targetType = string);
        return response;
    }
    #
    # + versionId - Version Id
    # + versionLimit - Version Limit
    # + return - Ok
    remote isolated function operationId05(int versionId, int versionLimit) returns string|error {
        string  path = string `/v1/${versionId}/v2/${versionLimit}`;
        string response = check self.clientEp-> get(path, targetType = string);
        return response;
    }
}