    package com.grocery.travel_app.exception;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RestControllerAdvice;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .error("Resource Not Found")
                    .message(ex.getMessage())
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        @ExceptionHandler(DuplicateResourceException.class)
        public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .error("Duplicate Resource")
                    .message(ex.getMessage())
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error("Bad Request")
                    .message(ex.getMessage())
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        @ExceptionHandler(ExternalApiException.class)
        public ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException ex) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.BAD_GATEWAY.value())
                    .error("External API Error")
                    .message(ex.getMessage())
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
        }
    }
