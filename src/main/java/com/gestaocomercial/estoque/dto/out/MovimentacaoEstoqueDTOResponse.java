package com.gestaocomercial.estoque.dto.out;

import com.gestaocomercial.estoque.enums.TipoMovimentacaoEnums;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MovimentacaoEstoqueDTOResponse {

    private Long id;
    private Long produtoId;
    private TipoMovimentacaoEnums tipoMoviementacaoEnums;
    private BigDecimal quantidade;
    private LocalDateTime data;
}
