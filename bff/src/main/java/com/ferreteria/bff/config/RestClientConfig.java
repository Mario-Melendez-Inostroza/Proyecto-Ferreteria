package com.ferreteria.bff.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean("authRestClient")
    public RestClient authRestClient(RestClient.Builder loadBalancedRestClientBuilder) {
        return loadBalancedRestClientBuilder.clone().baseUrl("http://ms-auth").build();
    }

    @Bean("productRestClient")
    public RestClient productRestClient(RestClient.Builder loadBalancedRestClientBuilder) {
        return loadBalancedRestClientBuilder.clone().baseUrl("http://ms-product").build();
    }

    @Bean("inventoryRestClient")
    public RestClient inventoryRestClient(RestClient.Builder loadBalancedRestClientBuilder) {
        return loadBalancedRestClientBuilder.clone().baseUrl("http://ms-inventory").build();
    }

    @Bean("salesRestClient")
    public RestClient salesRestClient(RestClient.Builder loadBalancedRestClientBuilder) {
        return loadBalancedRestClientBuilder.clone().baseUrl("http://ms-sales").build();
    }

    @Bean("notificationRestClient")
    public RestClient notificationRestClient(RestClient.Builder loadBalancedRestClientBuilder) {
        return loadBalancedRestClientBuilder.clone().baseUrl("http://ms-notification").build();
    }
}
