package com.gestaocomercial.produto.controller;

import com.gestaocomercial.produto.dto.in.ProdutoDTORequest;
import com.gestaocomercial.produto.dto.out.ProdutoDTOResponse;
import com.gestaocomercial.produto.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoDTOResponse> salvaProduto(
            @RequestBody @Valid ProdutoDTORequest produtoDTORequest ){
        return ResponseEntity.status(
                HttpStatus.CREATED).body(produtoService.cadastrarProduto(produtoDTORequest
        ));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTOResponse>> listarProduto(
            @RequestParam(required = false) String nome){
        return ResponseEntity.ok(produtoService.listarProdutos(nome));
    }

    @GetMapping("/codigo-barras/{codigoBarras}")
    public ResponseEntity<ProdutoDTOResponse> listarProdutoCodigoBarras(
            @PathVariable String codigoBarras){
        return ResponseEntity.ok(produtoService.buscarProdutoCodigoBarra(codigoBarras));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTOResponse> listarProdutoId(
            @PathVariable long id){
        return ResponseEntity.ok(produtoService.buscarProdutoPorId(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTOResponse> atualizarProduto(
            @PathVariable long id, @RequestBody @Valid ProdutoDTORequest dto){
        return ResponseEntity.ok(produtoService.atualizarProduto(id, dto));
    }


}
