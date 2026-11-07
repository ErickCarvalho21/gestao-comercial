package com.gestaocomercial.api.converter;

import com.gestaocomercial.api.request.ProdutoDTORequestFixture;
import com.gestaocomercial.api.response.ProdutoDTOResponseFixture;
import com.gestaocomercial.produto.Converter.ProdutoConverter;
import com.gestaocomercial.produto.dto.in.ProdutoDTORequest;
import com.gestaocomercial.produto.dto.out.ProdutoDTOResponse;
import com.gestaocomercial.produto.entity.Produto;
import com.gestaocomercial.produto.enums.TipoVendaEnums;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProdutoConverterTest {

    @InjectMocks
    private ProdutoConverter produtoConverter;

    private Produto produtoEntity;

    private ProdutoDTORequest produtoDTORequest;

    private ProdutoDTOResponse produtoDTOResponse;

    private LocalDateTime dataHora;

    @BeforeEach
    void setup() {
        dataHora = LocalDateTime.of(2023, 10, 5, 14, 15);

        produtoEntity = Produto.builder()
                .nome("Arroz")
                .codigoBarras("7891234567890")
                .descricao("Pacote de arroz de 1 kg")
                .tipoVendaEnums(TipoVendaEnums.UNIDADE)
                .precoCompra(new BigDecimal("4.00"))
                .precoVenda(new BigDecimal("6.00"))
                .quantidadeEstoque(BigDecimal.ZERO)
                .estoqueMinimo(new BigDecimal("5"))
                .build();

        produtoDTORequest = ProdutoDTORequestFixture.build(
                "Arroz",
                "7891234567890",
                "Pacote de arroz de 1 kg",
                TipoVendaEnums.UNIDADE,
                new BigDecimal("4.00"),
                new BigDecimal("6.00"),
                new BigDecimal("5")
        );

        produtoEntity = Produto.builder()
                .id(1L)
                .nome("Arroz")
                .codigoBarras("7891234567890")
                .descricao("Pacote de arroz de 1 kg")
                .tipoVendaEnums(TipoVendaEnums.UNIDADE)
                .precoCompra(new BigDecimal("4.00"))
                .precoVenda(new BigDecimal("6.00"))
                .quantidadeEstoque(new BigDecimal("10"))
                .estoqueMinimo(new BigDecimal("5"))
                .ativo(true)
                .dataCadastro(dataHora)
                .build();

        produtoDTOResponse = ProdutoDTOResponseFixture.build(
                1L,
                "Arroz",
                "7891234567890",
                "Pacote de arroz de 1 kg",
                TipoVendaEnums.UNIDADE,
                new BigDecimal("4.00"),
                new BigDecimal("6.00"),
                new BigDecimal("10"),
                new BigDecimal("5"),
                true,
                dataHora
        );
    }



    @Test
    void deveConverterParaProdutoEntity() {
        Produto entity = produtoConverter.paraProduto(produtoDTORequest);
        assertEquals(produtoEntity, entity);
    }

    @Test
    void deveConverterParaProdutoDTOResponse() {
        ProdutoDTOResponse dto = produtoConverter.paraProdutoDTOResponse(produtoEntity);
        assertEquals(produtoDTOResponse, dto);
    }

}
