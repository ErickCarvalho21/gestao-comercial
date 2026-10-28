package com.gestaocomercial.caixa.controller;

import com.gestaocomercial.caixa.dtos.in.CaixaRequestDTO;
import com.gestaocomercial.caixa.dtos.out.CaixaResponseDTO;
import com.gestaocomercial.caixa.service.CaixaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caixa")
@RequiredArgsConstructor

public class CaixaController {

    private final CaixaService caixaService;

    @PostMapping
    public ResponseEntity<CaixaResponseDTO> abrirCaixa(@RequestBody @Valid CaixaRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED).body(caixaService.abrirCaixa(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CaixaResponseDTO> buscarCaixaId(@PathVariable Long caixaId){
        return ResponseEntity.ok(caixaService.buscarCaixaPorId(caixaId));
    }

    @GetMapping("/{id}/aberto")
    public  ResponseEntity<CaixaResponseDTO> buscarCaixaAberto(){
        return ResponseEntity.ok(caixaService.buscarCaixaAberto());
    }

    @PatchMapping("/fechar")
    public  ResponseEntity<CaixaResponseDTO> fecharCaixa(){
        return ResponseEntity.ok(caixaService.fecharCaixa());
    }



}
