package com.ferreteria.bff.controller;

import com.ferreteria.bff.dto.PricingRequestDto;
import com.ferreteria.bff.dto.PricingResponseDto;
import com.ferreteria.bff.service.PricingBffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BFF controller exposing pricing/discount calculation endpoints to the
 * frontend, delegating all operations to the pricing microservice through
 * {@link PricingBffService}.
 */
@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
@Tag(name = "Pricing", description = "Endpoints for pricing and discount calculation")
public class PricingController {

    private final PricingBffService pricingBffService;

    /**
     * Calculates the discounted price for a product, quantity and category.
     *
     * @param dto the pricing calculation input
     * @return the calculated pricing result
     */
    @Operation(
            summary = "Calculate price",
            description = "Calculates the discounted price by forwarding the request to the pricing service.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Pricing calculation input",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pricing request"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/calculate")
    public ResponseEntity<PricingResponseDto> calculate(@Valid @RequestBody PricingRequestDto dto) {
        return ResponseEntity.ok(pricingBffService.calculate(dto));
    }
}
