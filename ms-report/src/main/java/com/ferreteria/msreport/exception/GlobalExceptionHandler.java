package com.ferreteria.msreport.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Minimal global exception handler that prevents raw stack traces from
 * leaking to clients when a downstream service (ms-sales, ms-inventory,
 * ms-product) fails or is unreachable.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles any unexpected exception raised while building a report.
     *
     * @param ex the exception raised
     * @return a 500 response with a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        log.error("Error inesperado generando reporte: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Ocurrio un error al generar el reporte"));
    }
}
