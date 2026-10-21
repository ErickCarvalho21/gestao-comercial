package com.gestaocomercial.caixa.dtos.in;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CaixaRequestDTO {


    @NotNull
    @PositiveOrZero
    private BigDecimal saldoInicial;
}
