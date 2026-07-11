package com.ferreteria.mspricing.controller;

import com.ferreteria.mspricing.dto.PricingRequestDto;
import com.ferreteria.mspricing.dto.PricingResponseDto;
import com.ferreteria.mspricing.service.PricingService;
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
 * REST controller exposing pricing/discount calculation for products.
 */
@RestController
@RequestMapping("/pricing")
@RequiredArgsConstructor
@Tag(name = "Pricing", description = "Endpoints for price and discount calculation")
public class PricingController {

    private final PricingService pricingService;

    /**
     * Calculates the applicable discount and final price for a product given
     * its unit price, quantity and (optionally) category.
     *
     * @param dto the pricing request data
     * @return the calculated pricing result with HTTP 200 status
     */
    @Operation(
            summary = "Calculate pricing",
            description = "Calculates the applicable discount and final price for a product.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Pricing data to calculate",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pricing calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pricing request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/calculate")
    public ResponseEntity<PricingResponseDto> calculate(@Valid @RequestBody PricingRequestDto dto) {
        return ResponseEntity.ok(pricingService.calcular(dto));
    }
}
