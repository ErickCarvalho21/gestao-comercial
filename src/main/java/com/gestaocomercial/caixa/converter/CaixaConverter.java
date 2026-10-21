package com.gestaocomercial.caixa.converter;


import com.gestaocomercial.caixa.dtos.in.CaixaRequestDTO;
import com.gestaocomercial.caixa.dtos.out.CaixaResponseDTO;
import com.gestaocomercial.caixa.entity.Caixa;
import com.gestaocomercial.venda.converter.VendaConverter;
import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.entity.Venda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CaixaConverter {

    private final VendaConverter vendaConverter;

    public Caixa converterCaixa (CaixaRequestDTO caixaDTO){

        return Caixa.builder()
                .saldoInicial(caixaDTO.getSaldoInicial())
                .build();
    }

    public CaixaResponseDTO paraCaixaResponseDTO(Caixa caixa){

        List<VendaDTOResponse> caixaDTO = caixa.getVendas().stream()
                .map(vendaConverter::paraVendaDTOResponse).toList();

        return CaixaResponseDTO.builder()
                .id(caixa.getId())
                .saldoInicial(caixa.getSaldoInicial())
                .saldoFinal(caixa.getSaldoFinal())
                .dataAbertura(caixa.getDataAbertura())
                .dataFechamento(caixa.getDataFechamento())
                .statusCaixaEnums(caixa.getStatusCaixaEnums())
                .vendas(caixaDTO)
                .build();
    }





}
