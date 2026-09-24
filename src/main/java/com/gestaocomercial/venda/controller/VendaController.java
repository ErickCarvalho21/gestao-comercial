package com.gestaocomercial.venda.controller;


import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
