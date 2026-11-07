package com.gestaocomercial.relatorio.converter;


import com.gestaocomercial.caixa.entity.Caixa;
import com.gestaocomercial.relatorio.dto.out.ResumoCaixaDTOResponse;
import org.springframework.stereotype.Component;

@Component
public class RelatorioConverter {

    public ResumoCaixaDTOResponse paraResumoCaixaDTO(Caixa caixa) {

        return ResumoCaixaDTOResponse.builder()
                .caixaId(caixa.getId())
                .saldoInicial(caixa.getSaldoInicial())
                .totalDinheiro(caixa.getTotalDinheiro())
                .totalPix(caixa.getTotalPix())
                .totalCartao(caixa.getTotalCartao())
                .totalVendido(caixa.getTotalVendido())
                .saldoFinal(caixa.getSaldoFinal())
                .dataAbertura(caixa.getDataAbertura())
                .dataFechamento(caixa.getDataFechamento())
                .funcionario(caixa.getUsuario().getNome())
                .build();
    }
}
