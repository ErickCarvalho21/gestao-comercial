package com.gestaocomercial.venda.controller;


import com.gestaocomercial.venda.dto.in.AlterarQuantidadeItemDTORequest;
import com.gestaocomercial.venda.dto.in.ItemVendaDTORequest;
import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaController {
    private final VendaService vendaService;


    @PostMapping
    public ResponseEntity<VendaDTOResponse> abrirVenda(){
        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(vendaService.abrirVenda());
    }


    @PostMapping("{id}/itens")
    public ResponseEntity<VendaDTOResponse> adicionaIten(@PathVariable Long id,
                                                         @RequestBody @Valid ItemVendaDTORequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vendaService.adicionarItem(id, request));

    }

    @PatchMapping("/vendas/{vendaId}/itens/{itemId}")
    public ResponseEntity<VendaDTOResponse> alterarItens(@PathVariable Long vendaId,
                                                         @PathVariable Long itemId,
                                                         @RequestBody @Valid AlterarQuantidadeItemDTORequest request){
        return ResponseEntity.ok(vendaService.alterarQuantidade(vendaId, itemId, request));
    }

    @DeleteMapping("/{vendaId}/itens/{itemId}")
    public ResponseEntity<VendaDTOResponse> removeItens(@PathVariable Long vendaId, @PathVariable Long itemId){

        return ResponseEntity.ok(vendaService.removerItem(vendaId, itemId));
    }


}
