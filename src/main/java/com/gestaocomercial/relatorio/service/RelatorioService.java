package com.gestaocomercial.relatorio.service;


import com.gestaocomercial.estoque.dto.out.MovimentacaoEstoqueDTOResponse;
import com.gestaocomercial.estoque.service.EstoqueService;
import com.gestaocomercial.produto.dto.out.ProdutoDTOResponse;
import com.gestaocomercial.produto.service.ProdutoService;
import com.gestaocomercial.relatorio.dto.out.ProdutoMaisVendidoDTOResponse;
import com.gestaocomercial.venda.converter.VendaConverter;
import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.entity.ItemVenda;
import com.gestaocomercial.venda.entity.Venda;
import com.gestaocomercial.venda.enums.FormaPagamentoEnums;
import com.gestaocomercial.venda.enums.StatusVendaEnums;
import com.gestaocomercial.venda.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final VendaRepository vendaRepository;
    private final VendaConverter vendaConverter;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    public List<VendaDTOResponse> listarVendasDoDia() {

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime inicioDoProximoDia = hoje.plusDays(1).atStartOfDay();

        List<Venda> vendas =
                vendaRepository.findByDataVendaBetween(
                        inicioDoDia,
                        inicioDoProximoDia
                );

        return vendas.stream()
                .map(vendaConverter::paraVendaDTOResponse)
                .toList();
    }


    public List<VendaDTOResponse> listarVendasPorPeriodo(
            LocalDate dataInicial,
            LocalDate dataFinal
    ) {

        LocalDateTime inicio =
                dataInicial.atStartOfDay();

        LocalDateTime fim =
                dataFinal.plusDays(1).atStartOfDay();

        List<Venda> vendas =
                vendaRepository.findByDataVendaBetween(
                        inicio,
                        fim
                );

        return vendas.stream()
                .map(vendaConverter::paraVendaDTOResponse)
                .toList();
    }

    public BigDecimal calcularFaturamentoDiario() {

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime inicioDoProximoDia = hoje.plusDays(1).atStartOfDay();

        List<Venda> vendas =
                vendaRepository.findByDataVendaBetween(
                        inicioDoDia,
                        inicioDoProximoDia
                );

        BigDecimal faturamento = BigDecimal.ZERO;

        for (Venda venda : vendas) {

            if (venda.getStatusVendasEnums() == StatusVendaEnums.FINALIZADA) {
                faturamento = faturamento.add(venda.getValorTotal());
            }
        }

        return faturamento;
    }

    public BigDecimal calcularFaturamentoPorPeriodo(
            LocalDate dataInicial,
            LocalDate dataFinal
    ) {

        LocalDateTime inicio =
                dataInicial.atStartOfDay();

        LocalDateTime fim =
                dataFinal.plusDays(1).atStartOfDay();

        List<Venda> vendas =
                vendaRepository.findByDataVendaBetween(
                        inicio,
                        fim
                );

        BigDecimal faturamento = BigDecimal.ZERO;

        for (Venda venda : vendas) {

            if (venda.getStatusVendasEnums() == StatusVendaEnums.FINALIZADA) {
                faturamento = faturamento.add(venda.getValorTotal());
            }
        }

        return faturamento;
    }

    public BigDecimal calcularVendasPorFormaPagamento(
            FormaPagamentoEnums formaPagamento
    ) {

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime inicioDoProximoDia = hoje.plusDays(1).atStartOfDay();

        List<Venda> vendas =
                vendaRepository.findByDataVendaBetween(
                        inicioDoDia,
                        inicioDoProximoDia
                );

        BigDecimal total = BigDecimal.ZERO;

        for (Venda venda : vendas) {

            if (venda.getStatusVendasEnums() == StatusVendaEnums.FINALIZADA
                    && venda.getFormaPagamentosEnums() == formaPagamento) {

                total = total.add(venda.getValorTotal());
            }
        }

        return total;
    }

    public List<ProdutoMaisVendidoDTOResponse> listarProdutosMaisVendidos() {

        List<Venda> vendasFinalizadas =
                vendaRepository.findByStatusVendasEnums(
                        StatusVendaEnums.FINALIZADA
                );

        Map<Long, ProdutoMaisVendidoDTOResponse> produtosAgrupados =
                new HashMap<>();

        for (Venda venda : vendasFinalizadas) {

            for (ItemVenda item : venda.getItens()) {

                Long produtoId = item.getProduto().getId();

                ProdutoMaisVendidoDTOResponse produto =
                        produtosAgrupados.get(produtoId);

                if (produto == null) {

                    produto = ProdutoMaisVendidoDTOResponse.builder()
                            .produtoId(produtoId)
                            .nomeProduto(item.getProduto().getNome())
                            .quantidadeVendida(BigDecimal.ZERO)
                            .build();

                    produtosAgrupados.put(produtoId, produto);
                }

                produto.setQuantidadeVendida(
                        produto.getQuantidadeVendida()
                                .add(item.getQuantidadeItem())
                );
            }
        }

        return produtosAgrupados.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                ProdutoMaisVendidoDTOResponse::getQuantidadeVendida
                        ).reversed()
                )
                .toList();
    }

    public List<ProdutoDTOResponse> listarProdutosEstoqueBaixo() {
        return produtoService.listarProdutoEstoqueBaixo();
    }

    public List<MovimentacaoEstoqueDTOResponse> listarHistoricoEstoque() {
        return estoqueService.listarMovimentacoes();
    }




}

