package com.gestaocomercial.relatorio;


import com.gestaocomercial.venda.converter.VendaConverter;
import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.entity.Venda;
import com.gestaocomercial.venda.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final VendaRepository vendaRepository;
    private final VendaConverter vendaConverter;

    public List<VendaDTOResponse> listarVendasDoDia() {

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime inicioDoProximoDia = hoje.plusDays(1).atStartOfDay();

        List<Venda> vendas =
                vendaRepository.findByDataVendaBetween(
                        inicioDoDia,
                        inicioDoProximoDia
                );

        return vendas.stream()
                .map(vendaConverter::paraVendaDTOResponse)
                .toList();
    }
}
}
