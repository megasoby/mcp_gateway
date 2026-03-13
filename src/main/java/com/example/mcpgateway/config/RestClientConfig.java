package com.example.mcpgateway.config;

import com.example.mcpgateway.tool.InventoryTool;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.StaticToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient inventoryRestClient(@Value("${inventory.api.base-url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("accept", "*/*")
                .build();
    }

    @Bean
    public ToolCallbackProvider inventoryToolProvider(InventoryTool inventoryTool) {
        return new StaticToolCallbackProvider(ToolCallbacks.from(inventoryTool));
    }
}
