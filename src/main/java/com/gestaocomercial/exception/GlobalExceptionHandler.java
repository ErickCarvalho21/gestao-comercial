package com.gestaocomercial.exception;


import com.gestaocomercial.exception.dto.ErrorDTOResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CodigoBarrasDuplicationException.class)
    public ResponseEntity<ErrorDTOResponse> handlerConflictException(CodigoBarrasDuplicationException ex,
                                                                       HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(
                        HttpStatus.CONFLICT,
                        ex.getMessage(),
                        request.getRequestURI(),
                        "Conflict"
                ));
    }

    private ErrorDTOResponse buildError (
            HttpStatus status, String mensagem, String path, String message){
        return ErrorDTOResponse.builder()
                .error(status.getReasonPhrase())
                .message(mensagem)
                .path(path)
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .build();
    }



}
