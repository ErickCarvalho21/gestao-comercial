package com.gestaocomercial.estoque.controller;

import com.gestaocomercial.estoque.dto.in.MovimentacaoEstoqueDTORequest;
import com.gestaocomercial.estoque.dto.out.MovimentacaoEstoqueDTOResponse;
import com.gestaocomercial.estoque.service.EstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/estoque")

public class EstoqueController {

    private final EstoqueService estoqueService;

    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueDTOResponse> registrarEstoque (
            @RequestBody @Valid MovimentacaoEstoqueDTORequest movimentacaoEstoqueDTORequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estoqueService.registrarMovimentacao(movimentacaoEstoqueDTORequest));

    }

    @GetMapping("/movimentacao")
    public ResponseEntity<List<MovimentacaoEstoqueDTOResponse>> listarMovimentacoes(){
        return ResponseEntity.ok(estoqueService.listarMovimentacoes());
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<List<MovimentacaoEstoqueDTOResponse>> listarMovimentacoesID(@PathVariable Long produtoId){
        return ResponseEntity.ok(estoqueService.listarMovimentacaoPorId(produtoId));

    }

}
