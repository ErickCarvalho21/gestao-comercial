package com.gestaocomercial.estoque.dto.in;


import com.gestaocomercial.estoque.enums.TipoMovimentacaoEnums;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MovimentacaoEstoqueDTORequest {

    @NotNull
    private Long produtoId;
    @NotNull
    private TipoMovimentacaoEnums tipoMovimentacaoEnums;
    @NotNull @PositiveOrZero
    private BigDecimal quantidade;

}
