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
                        request.getRequestURI()
                ));
    }


    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<ErrorDTOResponse> handlerNotFoundException(ProdutoNaoEncontradoException ex,
                                                                     HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    private ErrorDTOResponse buildError (
            HttpStatus status, String mensagem, String path){
        return ErrorDTOResponse.builder()
                .error(status.getReasonPhrase())
                .message(mensagem)
                .path(path)
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .build();
    }



}
