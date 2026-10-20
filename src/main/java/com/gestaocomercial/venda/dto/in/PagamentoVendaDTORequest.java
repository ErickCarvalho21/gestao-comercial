package com.gestaocomercial.venda.dto.in;

import com.gestaocomercial.venda.enums.FormaPagamentoEnums;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoVendaDTORequest {


    @NotNull
    private FormaPagamentoEnums formaPagamentoEnums;
}
