package com.iteo.priceservice.controlleradvice;

import com.iteo.priceservice.controlleradvice.models.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EntityNotFoundExceptionControllerAdvice<T extends EntityNotFoundException> {

    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(T exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

}
