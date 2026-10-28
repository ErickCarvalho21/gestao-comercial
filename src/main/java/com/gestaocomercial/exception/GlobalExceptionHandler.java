package com.gestaocomercial.exception;


import com.gestaocomercial.exception.dto.ErrorDTOResponse;
import com.gestaocomercial.venda.entity.Venda;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<ErrorDTOResponse> handlerConflictException(
            EstoqueInsuficienteException ex, HttpServletRequest request){
        return
                ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(
        HttpStatus.CONFLICT,
        ex.getMessage(),
        request.getRequestURI()
        ));
    }

    @ExceptionHandler(QuantidadeMovimentacaoInvalidaException.class)
    public ResponseEntity<ErrorDTOResponse> handlerBadRequestException(
            QuantidadeMovimentacaoInvalidaException ex, HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTOResponse> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildError(
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult().getFieldError().getDefaultMessage(),
                request.getRequestURI()
        ));
    }
    @ExceptionHandler(VendaNaoEncontradaException.class)
    public ResponseEntity<ErrorDTOResponse> handlerNotFoundException(
            VendaNaoEncontradaException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(ItenVendaNaoEncontradoException.class)
    public ResponseEntity<ErrorDTOResponse> handlerNotFoundException(ItenVendaNaoEncontradoException ex,
                                                                       HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(QuantidadeItemVendaInvalidaException.class)
    public ResponseEntity<ErrorDTOResponse> handlerBadRequestException(QuantidadeItemVendaInvalidaException ex,
                                                                       HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(ItemVendaNaoPertenceAVendaException.class)
    public ResponseEntity<ErrorDTOResponse> handlerConflictException(ItemVendaNaoPertenceAVendaException ex,
                                                                     HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(StatusVendaInvalidoException.class)
    public ResponseEntity<ErrorDTOResponse> handlerConflictException(StatusVendaInvalidoException ex,
                                                                     HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(ProdutoInativoException.class)
    public ResponseEntity<ErrorDTOResponse> handlerConflictException(ProdutoInativoException ex,
                                                                     HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(VendaSemItensException.class)
    public ResponseEntity<ErrorDTOResponse> handlerConflictException(VendaSemItensException ex,
                                                                     HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(FormaPagamentoNaoDefinidaException.class)
    public ResponseEntity<ErrorDTOResponse> handlerConflictException(FormaPagamentoNaoDefinidaException ex,
                                                                     HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(CaixaIndisponivelException.class)
    public ResponseEntity<ErrorDTOResponse> handlerConflictException(CaixaIndisponivelException ex,
                                                                     HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    @ExceptionHandler(CaixaNaoEncontradoException.class)
    public ResponseEntity<ErrorDTOResponse> handlerNotFoundException(CaixaNaoEncontradoException ex,
                                                                     HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }


    @ExceptionHandler(CaixaAbertoNaoEncontradoException.class)
    public ResponseEntity<ErrorDTOResponse> handlerNotFoundException(CaixaAbertoNaoEncontradoException ex,
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
