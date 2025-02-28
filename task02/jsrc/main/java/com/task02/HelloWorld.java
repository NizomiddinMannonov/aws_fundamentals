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
    isPublishVersion = true,
    aliasName = "${lambdas_alias_name}",
    logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class HelloWorld implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {
        System.out.println("Received event: " + event);  // Debugging log to see what input is coming in

        // Extract request details
        String path = (String) event.getOrDefault("rawPath", "/");
        Map<String, Object> requestContext = (Map<String, Object>) event.get("requestContext");
        Map<String, String> http = (Map<String, String>) requestContext.get("http");
        String method = http.get("method");

        // Debugging logs
        System.out.println("Extracted Path: " + path);
        System.out.println("Extracted Method: " + method);

        Map<String, Object> response = new HashMap<>();
        response.put("headers", Map.of("Content-Type", "application/json"));

        if ("/hello".equals(path) && "GET".equalsIgnoreCase(method)) {
            response.put("statusCode", 200);
            response.put("body", "{\"message\": \"Hello from Lambda\"}");
        } else {
            String errorMessage = String.format("Bad request syntax or unsupported method. Request path: %s. HTTP method: %s", path, method);
            response.put("statusCode", 400);
            response.put("body", String.format("{\"message\": \"%s\"}", errorMessage));
        }

        return response;
    }
}
