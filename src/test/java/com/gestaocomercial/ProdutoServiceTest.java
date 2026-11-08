package com.gestaocomercial;

import com.gestaocomercial.api.request.ProdutoDTORequestFixture;
import com.gestaocomercial.api.response.ProdutoDTOResponseFixture;
import com.gestaocomercial.exception.CodigoBarrasDuplicationException;
import com.gestaocomercial.exception.ProdutoNaoEncontradoException;
import com.gestaocomercial.produto.Converter.ProdutoConverter;
import com.gestaocomercial.produto.dto.in.ProdutoDTORequest;
import com.gestaocomercial.produto.dto.out.ProdutoDTOResponse;
import com.gestaocomercial.produto.entity.Produto;
import com.gestaocomercial.produto.enums.TipoVendaEnums;
import com.gestaocomercial.produto.repository.ProdutoRepository;
import com.gestaocomercial.produto.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoConverter produtoConverter;

    private ProdutoDTORequest produtoDTORequest;
    private Produto produtoEntity;
    private ProdutoDTOResponse produtoDTOResponse;

    @BeforeEach
    void setup() {
        LocalDateTime dataHora =
                LocalDateTime.of(2023, 10, 5, 14, 15);

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
                .nome("Arroz")
                .codigoBarras("7891234567890")
                .descricao("Pacote de arroz de 1 kg")
                .tipoVendaEnums(TipoVendaEnums.UNIDADE)
                .precoCompra(new BigDecimal("4.00"))
                .precoVenda(new BigDecimal("6.00"))
                .quantidadeEstoque(BigDecimal.ZERO)
                .estoqueMinimo(new BigDecimal("5"))
                .build();

        produtoDTOResponse = ProdutoDTOResponseFixture.build(
                1L,
                "Arroz",
                "7891234567890",
                "Pacote de arroz de 1 kg",
                TipoVendaEnums.UNIDADE,
                new BigDecimal("4.00"),
                new BigDecimal("6.00"),
                BigDecimal.ZERO,
                new BigDecimal("5"),
                true,
                dataHora
        );
    }

    @Test
    void deveCadastrarProdutoComSucesso() {
        when(produtoRepository.existsByCodigoBarras(
                produtoDTORequest.getCodigoBarras()
        )).thenReturn(false);

        when(produtoConverter.paraProduto(produtoDTORequest))
                .thenReturn(produtoEntity);

        when(produtoRepository.save(produtoEntity))
                .thenReturn(produtoEntity);

        when(produtoConverter.paraProdutoDTOResponse(produtoEntity))
                .thenReturn(produtoDTOResponse);

        ProdutoDTOResponse dto =
                produtoService.cadastrarProduto(produtoDTORequest);

        assertEquals(produtoDTOResponse, dto);

        verify(produtoRepository).existsByCodigoBarras(
                produtoDTORequest.getCodigoBarras()
        );
        verify(produtoConverter).paraProduto(produtoDTORequest);
        verify(produtoRepository).save(produtoEntity);
        verify(produtoConverter).paraProdutoDTOResponse(produtoEntity);
    }

    @Test
    void deveGerarExcecaoCasoCodigoBarrasJaExista() {
        when(produtoRepository.existsByCodigoBarras(
                produtoDTORequest.getCodigoBarras()
        )).thenReturn(true);

        CodigoBarrasDuplicationException e = assertThrows(
                CodigoBarrasDuplicationException.class,
                () -> produtoService.cadastrarProduto(produtoDTORequest)
        );

        assertEquals(
                "Este Codigo de Barras ja esta cadrastado",
                e.getMessage()
        );

        verify(produtoRepository).existsByCodigoBarras(
                produtoDTORequest.getCodigoBarras()
        );
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }
    @Test
    void deveGerarExcecaoCasoOcorraErroAoGravarProduto() {
        when(produtoRepository.existsByCodigoBarras(
                produtoDTORequest.getCodigoBarras()
        )).thenReturn(false);

        when(produtoConverter.paraProduto(produtoDTORequest))
                .thenReturn(produtoEntity);

        when(produtoRepository.save(produtoEntity))
                .thenThrow(new RuntimeException(
                        "Falha ao gravar dados do produto"
                ));

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> produtoService.cadastrarProduto(produtoDTORequest)
        );

        assertEquals("Falha ao gravar dados do produto", e.getMessage());

        verify(produtoRepository).existsByCodigoBarras(
                produtoDTORequest.getCodigoBarras()
        );
        verify(produtoConverter).paraProduto(produtoDTORequest);
        verify(produtoRepository).save(produtoEntity);
        verifyNoMoreInteractions(produtoRepository, produtoConverter);
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        Long id = 1L;

        when(produtoRepository.findById(id))
                .thenReturn(Optional.of(produtoEntity));

        when(produtoConverter.paraProdutoDTOResponse(produtoEntity))
                .thenReturn(produtoDTOResponse);

        ProdutoDTOResponse dto = produtoService.buscarProdutoPorId(id);

        verify(produtoRepository).findById(id);
        verify(produtoConverter).paraProdutoDTOResponse(produtoEntity);

        assertEquals(produtoDTOResponse, dto);
    }
    @Test
    void deveGerarExcecaoCasoProdutoNaoSejaEncontradoPorId() {
        Long id = 1L;

        when(produtoRepository.findById(id))
                .thenReturn(Optional.empty());

        ProdutoNaoEncontradoException e = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> produtoService.buscarProdutoPorId(id)
        );

        assertEquals("Produto não Encontrado: " + id, e.getMessage());

        verify(produtoRepository).findById(id);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }
    @Test
    void deveGerarExcecaoCasoOcorraErroAoBuscarProdutoPorId() {
        Long id = 1L;

        when(produtoRepository.findById(id))
                .thenThrow(new RuntimeException(
                        "Falha ao buscar dados do produto"
                ));

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> produtoService.buscarProdutoPorId(id)
        );

        assertEquals("Falha ao buscar dados do produto", e.getMessage());

        verify(produtoRepository).findById(id);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }
}
