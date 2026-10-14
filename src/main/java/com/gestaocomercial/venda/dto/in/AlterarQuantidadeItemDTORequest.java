package com.gestaocomercial.venda.dto.in;


import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlterarQuantidadeItemDTORequest {


    @NonNull
    @Positive
    private BigDecimal quantidadeItem;
}
