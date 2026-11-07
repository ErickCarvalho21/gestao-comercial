package com.gestaocomercial.relatorio.controller;


import com.gestaocomercial.estoque.dto.out.MovimentacaoEstoqueDTOResponse;
import com.gestaocomercial.produto.dto.out.ProdutoDTOResponse;
import com.gestaocomercial.relatorio.dto.out.ProdutoMaisVendidoDTOResponse;
import com.gestaocomercial.relatorio.service.RelatorioService;
import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.enums.FormaPagamentoEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @GetMapping("/vendas/periodo")
    public ResponseEntity<List<VendaDTOResponse>> listarVendasPorPeriodo(
            @RequestParam LocalDate dataInicial,
            @RequestParam LocalDate dataFinal
    ) {
        return ResponseEntity.ok(
                relatorioService.listarVendasPorPeriodo(dataInicial, dataFinal));
    }
    @GetMapping("/faturamento/diario")
    public ResponseEntity<BigDecimal> calcularFaturamentoDiario() {
        return ResponseEntity.ok(
                relatorioService.calcularFaturamentoDiario()
        );
    }
    @GetMapping("/faturamento/periodo")
    public ResponseEntity<BigDecimal> calcularFaturamentoPorPeriodo(
            @RequestParam LocalDate dataInicial,
            @RequestParam LocalDate dataFinal
    ) {
        return ResponseEntity.ok(
                relatorioService.calcularFaturamentoPorPeriodo(dataInicial, dataFinal)
        );
    }

    @GetMapping("/pagamentos")
    public ResponseEntity<BigDecimal> calcularVendasPorFormaPagamento(@RequestParam FormaPagamentoEnums pagamentoEnums){
        return ResponseEntity.ok(relatorioService.calcularVendasPorFormaPagamento(pagamentoEnums));
    }
    @GetMapping("/produtos/mais-vendidos")
    public ResponseEntity<List<ProdutoMaisVendidoDTOResponse>> listarProdutosMaisVendidos() {

        return ResponseEntity.ok(
                relatorioService.listarProdutosMaisVendidos()
        );
    }
    @GetMapping("/produtos/estoque-baixo")
    public ResponseEntity<List<ProdutoDTOResponse>> listarProdutosEstoqueBaixo() {

        return ResponseEntity.ok(
                relatorioService.listarProdutosEstoqueBaixo()
        );
    }

    @GetMapping("/estoque/historico")
    public ResponseEntity<List<MovimentacaoEstoqueDTOResponse>> listarHistoricoEstoque() {

        return ResponseEntity.ok(
                relatorioService.listarHistoricoEstoque()
        );
    }
}
