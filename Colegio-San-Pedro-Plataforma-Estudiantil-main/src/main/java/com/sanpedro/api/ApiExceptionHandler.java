package com.sanpedro.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.sanpedro.api")
public class ApiExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthentication(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales inválidas"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        HttpStatus status = isNotFound(ex.getMessage()) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        String message = ex.getMessage() == null || ex.getMessage().isBlank()
                ? "Se produjo un error al procesar la solicitud"
                : ex.getMessage();

        return ResponseEntity.status(status)
                .body(Map.of("error", message));
    }

    private boolean isNotFound(String message) {
        if (message == null) {
            return false;
        }

        String normalized = message.toLowerCase();
        return normalized.contains("no encontrado") || normalized.contains("no existe");
    }
}