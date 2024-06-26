import ballerina/http;

public isolated client class Client {
    final http:Client clientEp;
    final readonly & ApiKeysConfig apiKeyConfig;
    # Gets invoked to initialize the `connector`.
    #
    # + apiKeyConfig - API keys for authorization
    # + config - The configurations to be used when initializing the `connector`
    # + serviceUrl - URL of the target service
    # + return - An error if connector initialization failed
    public isolated function init(ApiKeysConfig apiKeyConfig, ConnectionConfig config =  {}, string serviceUrl = "https://petstore.swagger.io:443/v2") returns error? {
        http:ClientConfiguration httpClientConfig = {httpVersion: config.httpVersion, timeout: config.timeout, forwarded: config.forwarded, poolConfig: config.poolConfig, compression: config.compression, circuitBreaker: config.circuitBreaker, retryConfig: config.retryConfig, validation: config.validation};
        do {
            if config.http1Settings is ClientHttp1Settings {
                ClientHttp1Settings settings = check config.http1Settings.ensureType(ClientHttp1Settings);
                httpClientConfig.http1Settings = {...settings};
            }
            if config.http2Settings is http:ClientHttp2Settings {
                httpClientConfig.http2Settings = check config.http2Settings.ensureType(http:ClientHttp2Settings);
            }
            if config.cache is http:CacheConfig {
                httpClientConfig.cache = check config.cache.ensureType(http:CacheConfig);
            }
            if config.responseLimits is http:ResponseLimitConfigs {
                httpClientConfig.responseLimits = check config.responseLimits.ensureType(http:ResponseLimitConfigs);
            }
            if config.secureSocket is http:ClientSecureSocket {
                httpClientConfig.secureSocket = check config.secureSocket.ensureType(http:ClientSecureSocket);
            }
            if config.proxy is http:ProxyConfig {
                httpClientConfig.proxy = check config.proxy.ensureType(http:ProxyConfig);
            }
        }
        http:Client httpEp = check new (serviceUrl, httpClientConfig);
        self.clientEp = httpEp;
        self.apiKeyConfig = apiKeyConfig.cloneReadOnly();
        return;
    }
    # Delete a pet
    #
    # + headers - Headers to be sent with the request
    # + return - Ok response
    resource isolated function delete pets(map<string|string[]> headers = {}) returns error? {
        string resourcePath = string `/pets`;
        map<anydata> queryParam = {};
        queryParam["appid1"] = self.apiKeyConfig.appid1;
        queryParam["appid2"] = self.apiKeyConfig.appid2;
        resourcePath = resourcePath + check getPathForQueryParam(queryParam);
        return self.clientEp->delete(resourcePath, headers = headers);
    }
    # List all pets
    #
    # + headers - Headers to be sent with the request
    # + queries - Queries to be sent with the request
    # + return - An paged array of pets
    resource isolated function get pets(map<string|string[]> headers = {}, *ListPetsQueries queries) returns Pets|error {
        string resourcePath = string `/pets`;
        map<anydata> queryParam = {...queries};
        queryParam["appid1"] = self.apiKeyConfig.appid1;
        queryParam["appid2"] = self.apiKeyConfig.appid2;
        resourcePath = resourcePath + check getPathForQueryParam(queryParam);
        return self.clientEp->get(resourcePath, headers);
    }
    # Create a pet
    #
    # + headers - Headers to be sent with the request
    # + return - Null response
    resource isolated function post pets(map<string|string[]> headers = {}) returns error? {
        string resourcePath = string `/pets`;
        map<anydata> queryParam = {};
        queryParam["appid1"] = self.apiKeyConfig.appid1;
        queryParam["appid2"] = self.apiKeyConfig.appid2;
        resourcePath = resourcePath + check getPathForQueryParam(queryParam);
        http:Request request = new;
        return self.clientEp->post(resourcePath, request, headers);
    }
    # Update a pet
    #
    # + headers - Headers to be sent with the request
    # + return - Null response
    resource isolated function put pets(map<string|string[]> headers = {}) returns error? {
        string resourcePath = string `/pets`;
        map<anydata> queryParam = {};
        queryParam["appid1"] = self.apiKeyConfig.appid1;
        queryParam["appid2"] = self.apiKeyConfig.appid2;
        resourcePath = resourcePath + check getPathForQueryParam(queryParam);
        http:Request request = new;
        return self.clientEp->put(resourcePath, request, headers);
    }
}
