package com.gestaocomercial.relatorio.dto.out;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ResumoCaixaDTOResponse {

    private Long caixaId;

    private BigDecimal saldoInicial;

    private BigDecimal totalDinheiro;

    private BigDecimal totalPix;

    private BigDecimal totalCartao;

    private BigDecimal totalVendido;

    private BigDecimal saldoFinal;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    private String funcionario;
}
