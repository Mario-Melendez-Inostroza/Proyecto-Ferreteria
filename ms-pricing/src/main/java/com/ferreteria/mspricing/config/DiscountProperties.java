package com.ferreteria.mspricing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

/**
 * Configuration properties that define the discount rules applied by the
 * pricing calculation service. Values are externalized so discount thresholds
 * and percentages can be tuned without recompiling the application.
 */
@ConfigurationProperties(prefix = "pricing.discount")
public class DiscountProperties {

    private Volume volume = new Volume();
    private Category category = new Category();

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Volume-based discount rule: applied when the purchased quantity reaches
     * the configured threshold.
     */
    public static class Volume {
        private Integer threshold;
        private BigDecimal percentage;

        public Integer getThreshold() {
            return threshold;
        }

        public void setThreshold(Integer threshold) {
            this.threshold = threshold;
        }

        public BigDecimal getPercentage() {
            return percentage;
        }

        public void setPercentage(BigDecimal percentage) {
            this.percentage = percentage;
        }
    }

    /**
     * Category-based discount rule: applied when the product category matches
     * the configured promotional category.
     */
    public static class Category {
        private BigDecimal promocion;

        public BigDecimal getPromocion() {
            return promocion;
        }

        public void setPromocion(BigDecimal promocion) {
            this.promocion = promocion;
        }
    }
}
