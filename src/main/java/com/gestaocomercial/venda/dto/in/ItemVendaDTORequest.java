package com.gestaocomercial.venda.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ItemVendaDTORequest {

    @NotNull
    @Positive
    private BigDecimal quantidadeItem;
    @NotBlank
    private String codigoBarras;
}
