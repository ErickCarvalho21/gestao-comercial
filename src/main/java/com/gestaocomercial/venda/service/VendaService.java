package com.gestaocomercial.venda.service;

import com.gestaocomercial.venda.converter.VendaConverter;
import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.entity.Venda;
import com.gestaocomercial.venda.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final VendaConverter vendaConverter;

    public VendaDTOResponse abrirVenda(){

        Venda venda = Venda.builder().build();
        Venda vendaSalva = vendaRepository.save(venda);
        return vendaConverter.paraVendaDTOResponse(vendaSalva);

    }





}
