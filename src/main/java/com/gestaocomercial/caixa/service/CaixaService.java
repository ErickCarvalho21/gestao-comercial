package com.gestaocomercial.caixa.service;


import com.gestaocomercial.caixa.converter.CaixaConverter;
import com.gestaocomercial.caixa.dtos.in.CaixaRequestDTO;
import com.gestaocomercial.caixa.dtos.out.CaixaResponseDTO;
import com.gestaocomercial.caixa.entity.Caixa;
import com.gestaocomercial.caixa.enums.StatusCaixaEnums;
import com.gestaocomercial.caixa.repository.CaixaRepository;
import com.gestaocomercial.exception.CaixaIndisponivelException;
import com.gestaocomercial.venda.entity.Venda;
import com.gestaocomercial.venda.enums.FormaPagamentoEnums;
import com.gestaocomercial.venda.enums.StatusVendaEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CaixaService {

    private final CaixaRepository caixaRepository;
    private final CaixaConverter caixaConverter;


    public CaixaResponseDTO abrirCaixa(CaixaRequestDTO request){

        validarCaixaDisponivelParaAbertura();

        return caixaConverter.paraCaixaResponseDTO(
                caixaRepository.save(caixaConverter.converterCaixa(request)));
    }

    private void validarCaixaDisponivelParaAbertura(){

        if(caixaRepository.existsByStatusCaixaEnums(StatusCaixaEnums.ABERTO)){
            throw new CaixaIndisponivelException(
                    "ja existe caixa aberto no sistema"
            );
        }
    }

    public CaixaResponseDTO buscarCaixaPorId(Long caixaId){

        Caixa caixaEncontrado = caixaRepository.findById(caixaId).orElseThrow(()-> new CaixaIndisponivelException(
                "Caixa não encontrado" + caixaId
        ));

        return caixaConverter.paraCaixaResponseDTO(caixaEncontrado);

    }

    public CaixaResponseDTO buscarCaixaAberto(){

        Caixa caixaEncontrado = caixaRepository.findByStatusCaixaEnums(StatusCaixaEnums.ABERTO)
                .orElseThrow(()-> new CaixaIndisponivelException(
                "Caixa aberto não foi encontrado"
        ));

        return caixaConverter.paraCaixaResponseDTO(caixaEncontrado);
    }

    public Caixa buscarCaixaAbertoEntidade(){

        return caixaRepository.findByStatusCaixaEnums(StatusCaixaEnums.ABERTO)
                .orElseThrow(()-> new CaixaIndisponivelException(
                        "Caixa aberto não foi encontrado"
                ));
    }

    public BigDecimal calcularTotalPorFormaPagamento(Caixa caixa, FormaPagamentoEnums pagamentoEnums){

        BigDecimal total = BigDecimal.ZERO;

        for (Venda venda : caixa.getVendas()){
            if (venda.getStatusVendasEnums().equals(StatusVendaEnums.FINALIZADA)){
                if (venda.getFormaPagamentosEnums().equals(pagamentoEnums)){

                    total = total.add(venda.getValorTotal());
                }
            }
        }
        return total;

    }

    public BigDecimal calcularTotalVendido(Caixa caixa){

        BigDecimal totalVenda = BigDecimal.ZERO;

        for(Venda venda : caixa.getVendas()){
            if(venda.getStatusVendasEnums().equals(StatusVendaEnums.FINALIZADA)){

                totalVenda = totalVenda.add(venda.getValorTotal());

            }
        }
        return totalVenda;

    }



}
