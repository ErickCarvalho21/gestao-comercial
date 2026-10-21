package com.gestaocomercial.caixa.dtos.out;

import com.gestaocomercial.caixa.enums.StatusCaixaEnums;
import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.entity.Venda;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CaixaResponseDTO {

    private Long id;

    private StatusCaixaEnums statusCaixaEnums;

    private BigDecimal saldoInicial;

    private BigDecimal saldoFinal;

    private List<VendaDTOResponse> vendas;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

}
