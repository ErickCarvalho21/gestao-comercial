package com.gestaocomercial.estoque.dto.out;

import com.gestaocomercial.estoque.enums.TipoMovimentacaoEnums;
import com.gestaocomercial.produto.entity.Produto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MovimentacaoEstoqueDTOReponse {

    private Long id;
    private Produto produto;
    private TipoMovimentacaoEnums tipoMoviementacaoEnums;
    private BigDecimal quantidade;
    private LocalDateTime data;
}
