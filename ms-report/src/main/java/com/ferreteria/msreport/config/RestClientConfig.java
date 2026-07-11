package com.ferreteria.msreport.config;

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

    @Bean("salesRestClient")
    public RestClient salesRestClient(RestClient.Builder loadBalancedRestClientBuilder) {
        return loadBalancedRestClientBuilder.clone().baseUrl("http://ms-sales").build();
    }

    @Bean("inventoryRestClient")
    public RestClient inventoryRestClient(RestClient.Builder loadBalancedRestClientBuilder) {
        return loadBalancedRestClientBuilder.clone().baseUrl("http://ms-inventory").build();
    }

    @Bean("productRestClient")
    public RestClient productRestClient(RestClient.Builder loadBalancedRestClientBuilder) {
        return loadBalancedRestClientBuilder.clone().baseUrl("http://ms-product").build();
    }
}
