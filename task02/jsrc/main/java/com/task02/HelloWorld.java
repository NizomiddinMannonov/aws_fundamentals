package com.task02;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
    lambdaName = "hello_world",
    roleName = "hello_world-role",
    isPublishVersion = false,
    aliasName = "${lambdas_alias_name}",
    logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class HelloWorld implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {
        String path = (String) event.getOrDefault("rawPath", "/");
        String method = ((Map<String, String>) event.get("requestContext")).get("httpMethod");

        Map<String, Object> response = new HashMap<>();
        response.put("headers", Map.of("Content-Type", "application/json"));

        if ("/hello".equals(path) && "GET".equalsIgnoreCase(method)) {
            response.put("statusCode", 200);
            response.put("message", "{\"message\": \"Hello from Lambda\"}");
        } else {
            String errorMessage = String.format("Bad request syntax or unsupported method. Request path: %s. HTTP method: %s", path, method);
            response.put("statusCode", 400);
            response.put("message", String.format("{\"message\": \"%s\"}", errorMessage));
        }

        return response;
    }
}
