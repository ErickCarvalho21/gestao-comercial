package com.gestaocomercial.relatorio;


import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/vendas/dia")
    public ResponseEntity<List<VendaDTOResponse>> listarVendasDoDia() {
        return ResponseEntity.ok(
                relatorioService.listarVendasDoDia()
        );
    }
}
