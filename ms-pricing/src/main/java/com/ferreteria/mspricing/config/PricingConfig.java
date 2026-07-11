package com.ferreteria.mspricing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the discount configuration properties as a Spring bean.
 */
@Configuration
@EnableConfigurationProperties(DiscountProperties.class)
public class PricingConfig {
}
