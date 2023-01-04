package com.iteo.priceservice.controlleradvice;

import com.iteo.priceservice.controlleradvice.models.ValidationErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ConstraintViolationExceptionControllerAdvice<T extends ConstraintViolationException> {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ValidationErrorResponse>> handle(T exception) {

        return ResponseEntity.badRequest()
                .body(
                        exception.getConstraintViolations().stream()
                                .map(this::map)
                                .toList()
                );
    }

    private ValidationErrorResponse map(ConstraintViolation<?> violation) {
        String fieldPathWithoutRoot = violation.getPropertyPath().toString().replaceFirst("^\\w+\\.", "");
        return new ValidationErrorResponse(
                violation.getMessage(),
                null,
                fieldPathWithoutRoot
        );
    }

}
