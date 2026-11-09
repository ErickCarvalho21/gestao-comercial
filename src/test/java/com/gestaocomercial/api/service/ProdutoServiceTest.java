package com.gestaocomercial.api.service;

import com.gestaocomercial.api.request.ProdutoAtualizacaoDTORequestFixture;
import com.gestaocomercial.api.request.ProdutoDTORequestFixture;
import com.gestaocomercial.api.response.ProdutoDTOResponseFixture;
import com.gestaocomercial.exception.CodigoBarrasDuplicationException;
import com.gestaocomercial.exception.ProdutoNaoEncontradoException;
import com.gestaocomercial.produto.Converter.ProdutoConverter;
import com.gestaocomercial.produto.dto.in.ProdutoAtualizacaoDTORequest;
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
import java.util.List;
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

    private ProdutoAtualizacaoDTORequest produtoAtualizacaoDTORequest;

    private String codigoBarras;

    private Long id;

    private String novoCodigoBarras;

    private String nome;

    private List<Produto> produtos;

    private List<ProdutoDTOResponse> produtosDTOResponse;

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
        produtoAtualizacaoDTORequest = ProdutoAtualizacaoDTORequestFixture.build(
                "Arroz",
                codigoBarras,
                "Pacote de arroz de 1 kg",
                TipoVendaEnums.UNIDADE,
                new BigDecimal("4.00"),
                new BigDecimal("6.00"),
                new BigDecimal("5")
        );

        codigoBarras = "7891234567890";
        id = 1L;
        novoCodigoBarras = "7899876543210";
        nome = "Arroz";
        produtos = List.of(produtoEntity);
        produtosDTOResponse = List.of(produtoDTOResponse);
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
    @Test
    void deveBuscarProdutoPorCodigoBarrasComSucesso() {

        when(produtoRepository.findByCodigoBarras(codigoBarras))
                .thenReturn(Optional.of(produtoEntity));

        when(produtoConverter.paraProdutoDTOResponse(produtoEntity))
                .thenReturn(produtoDTOResponse);

        ProdutoDTOResponse dto =
                produtoService.buscarProdutoCodigoBarra(codigoBarras);

        verify(produtoRepository).findByCodigoBarras(codigoBarras);
        verify(produtoConverter).paraProdutoDTOResponse(produtoEntity);

        assertEquals(produtoDTOResponse, dto);
    }

    @Test
    void deveGerarExcecaoCasoCodigoBarrasNaoSejaEncontrado() {

        when(produtoRepository.findByCodigoBarras(codigoBarras))
                .thenReturn(Optional.empty());

        ProdutoNaoEncontradoException e = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> produtoService.buscarProdutoCodigoBarra(codigoBarras)
        );

        assertEquals(
                " Produto nao encontrado: " + codigoBarras,
                e.getMessage()
        );

        verify(produtoRepository).findByCodigoBarras(codigoBarras);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }
    @Test
    void deveGerarExcecaoCasoOcorraErroAoBuscarProdutoPorCodigoBarras() {
        when(produtoRepository.findByCodigoBarras(codigoBarras))
                .thenThrow(new RuntimeException(
                        "Falha ao buscar produto por codigo de barras"
                ));

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> produtoService.buscarProdutoCodigoBarra(codigoBarras)
        );

        assertEquals(
                "Falha ao buscar produto por codigo de barras",
                e.getMessage()
        );

        verify(produtoRepository).findByCodigoBarras(codigoBarras);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void deveAtualizarProdutoComSucessoMantendoCodigoBarras() {
        when(produtoRepository.findById(id))
                .thenReturn(Optional.of(produtoEntity));

        when(produtoRepository.save(produtoEntity))
                .thenReturn(produtoEntity);

        when(produtoConverter.paraProdutoDTOResponse(produtoEntity))
                .thenReturn(produtoDTOResponse);

        ProdutoDTOResponse dto = produtoService.atualizarProduto(
                id,
                produtoAtualizacaoDTORequest
        );

        assertEquals(produtoDTOResponse, dto);

        verify(produtoRepository).findById(id);

        verify(produtoConverter).updateProduto(
                produtoAtualizacaoDTORequest,
                produtoEntity
        );

        verify(produtoRepository).save(produtoEntity);
        verify(produtoConverter).paraProdutoDTOResponse(produtoEntity);

        verifyNoMoreInteractions(produtoRepository, produtoConverter);
    }

    @Test
    void deveAtualizarProdutoComNovoCodigoBarrasDisponivel() {
        produtoAtualizacaoDTORequest.setCodigoBarras(novoCodigoBarras);
        produtoDTOResponse.setCodigoBarras(novoCodigoBarras);

        when(produtoRepository.findById(id))
                .thenReturn(Optional.of(produtoEntity));

        when(produtoRepository.existsByCodigoBarrasAndIdNot(
                novoCodigoBarras,
                id
        )).thenReturn(false);

        when(produtoRepository.save(produtoEntity))
                .thenReturn(produtoEntity);

        when(produtoConverter.paraProdutoDTOResponse(produtoEntity))
                .thenReturn(produtoDTOResponse);

        ProdutoDTOResponse dto = produtoService.atualizarProduto(
                id,
                produtoAtualizacaoDTORequest
        );

        assertEquals(produtoDTOResponse, dto);

        verify(produtoRepository).findById(id);

        verify(produtoRepository).existsByCodigoBarrasAndIdNot(
                novoCodigoBarras,
                id
        );

        verify(produtoConverter).updateProduto(
                produtoAtualizacaoDTORequest,
                produtoEntity
        );

        verify(produtoRepository).save(produtoEntity);
        verify(produtoConverter).paraProdutoDTOResponse(produtoEntity);

        verifyNoMoreInteractions(produtoRepository, produtoConverter);
    }
    @Test
    void deveGerarExcecaoAoAtualizarComCodigoBarrasDuplicado() {
        produtoAtualizacaoDTORequest.setCodigoBarras(novoCodigoBarras);

        when(produtoRepository.findById(id))
                .thenReturn(Optional.of(produtoEntity));

        when(produtoRepository.existsByCodigoBarrasAndIdNot(
                novoCodigoBarras,
                id
        )).thenReturn(true);

        CodigoBarrasDuplicationException e = assertThrows(
                CodigoBarrasDuplicationException.class,
                () -> produtoService.atualizarProduto(
                        id,
                        produtoAtualizacaoDTORequest
                )
        );

        assertEquals("Este codigo de barras ja existe", e.getMessage());

        verify(produtoRepository).findById(id);

        verify(produtoRepository).existsByCodigoBarrasAndIdNot(
                novoCodigoBarras,
                id
        );

        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void deveGerarExcecaoAoAtualizarProdutoNaoEncontrado() {
        when(produtoRepository.findById(id))
                .thenReturn(Optional.empty());

        ProdutoNaoEncontradoException e = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> produtoService.atualizarProduto(
                        id,
                        produtoAtualizacaoDTORequest
                )
        );

        assertEquals("Produto não Encontrado: " + id, e.getMessage());

        verify(produtoRepository).findById(id);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }
    @Test
    void deveGerarExcecaoCasoOcorraErroAoBuscarProdutoParaAtualizacao() {
        when(produtoRepository.findById(id))
                .thenThrow(new RuntimeException(
                        "Falha ao buscar produto para atualizacao"
                ));

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> produtoService.atualizarProduto(
                        id,
                        produtoAtualizacaoDTORequest
                )
        );

        assertEquals(
                "Falha ao buscar produto para atualizacao",
                e.getMessage()
        );

        verify(produtoRepository).findById(id);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void deveGerarExcecaoCasoOcorraErroAoSalvarAtualizacao() {
        when(produtoRepository.findById(id))
                .thenReturn(Optional.of(produtoEntity));

        when(produtoRepository.save(produtoEntity))
                .thenThrow(new RuntimeException(
                        "Falha ao salvar atualizacao do produto"
                ));

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> produtoService.atualizarProduto(
                        id,
                        produtoAtualizacaoDTORequest
                )
        );

        assertEquals(
                "Falha ao salvar atualizacao do produto",
                e.getMessage()
        );

        verify(produtoRepository).findById(id);

        verify(produtoConverter).updateProduto(
                produtoAtualizacaoDTORequest,
                produtoEntity
        );

        verify(produtoRepository).save(produtoEntity);

        verifyNoMoreInteractions(produtoRepository, produtoConverter);
    }

    @Test
    void deveDesativarProdutoComSucesso() {
        produtoEntity.setAtivo(true);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoEntity));

        produtoService.desativarProduto(id);

        assertFalse(produtoEntity.isAtivo());

        verify(produtoRepository).findById(id);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }
    @Test
    void deveGerarExcecaoAoDesativarProdutoNaoEncontrado() {
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        ProdutoNaoEncontradoException e = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> produtoService.desativarProduto(id)
        );

        assertEquals("Produto não Encontrado: " + id, e.getMessage());

        verify(produtoRepository).findById(id);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void deveGerarExcecaoCasoOcorraErroAoBuscarProdutoParaDesativacao() {
        when(produtoRepository.findById(id))
                .thenThrow(new RuntimeException(
                        "Falha ao buscar produto para desativacao"
                ));

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> produtoService.desativarProduto(id)
        );

        assertEquals(
                "Falha ao buscar produto para desativacao",
                e.getMessage()
        );

        verify(produtoRepository).findById(id);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void deveAtivarProdutoComSucesso() {
        produtoEntity.setAtivo(false);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoEntity));

        produtoService.ativarProduto(id);

        assertTrue(produtoEntity.isAtivo());

        verify(produtoRepository).findById(id);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }
    @Test
    void deveGerarExcecaoAoAtivarProdutoNaoEncontrado() {
        when(produtoRepository.findById(id))
                .thenReturn(Optional.empty());

        ProdutoNaoEncontradoException e = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> produtoService.ativarProduto(id)
        );

        assertEquals("Produto não Encontrado: " + id, e.getMessage());

        verify(produtoRepository).findById(id);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }
    @Test
    void deveGerarExcecaoCasoOcorraErroAoBuscarProdutoParaAtivacao() {
        when(produtoRepository.findById(id))
                .thenThrow(new RuntimeException(
                        "Falha ao buscar produto para ativacao"
                ));

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> produtoService.ativarProduto(id)
        );

        assertEquals(
                "Falha ao buscar produto para ativacao",
                e.getMessage()
        );

        verify(produtoRepository).findById(id);
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }
    @Test
    void deveListarProdutosComEstoqueBaixo() {
        when(produtoRepository.buscarProdutoEstoqueBaixo())
                .thenReturn(produtos);

        when(produtoConverter.paraProdutoDTOResponse(produtoEntity))
                .thenReturn(produtoDTOResponse);

        List<ProdutoDTOResponse> dtos =
                produtoService.listarProdutoEstoqueBaixo();

        assertEquals(produtosDTOResponse, dtos);

        verify(produtoRepository).buscarProdutoEstoqueBaixo();
        verify(produtoConverter).paraProdutoDTOResponse(produtoEntity);
        verifyNoMoreInteractions(produtoRepository, produtoConverter);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremProdutosComEstoqueBaixo() {
        when(produtoRepository.buscarProdutoEstoqueBaixo())
                .thenReturn(List.of());

        List<ProdutoDTOResponse> dtos =
                produtoService.listarProdutoEstoqueBaixo();

        assertTrue(dtos.isEmpty());

        verify(produtoRepository).buscarProdutoEstoqueBaixo();
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void deveGerarExcecaoCasoOcorraErroAoListarProdutosComEstoqueBaixo() {
        when(produtoRepository.buscarProdutoEstoqueBaixo())
                .thenThrow(new RuntimeException(
                        "Falha ao listar produtos com estoque baixo"
                ));

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> produtoService.listarProdutoEstoqueBaixo()
        );

        assertEquals(
                "Falha ao listar produtos com estoque baixo",
                e.getMessage()
        );

        verify(produtoRepository).buscarProdutoEstoqueBaixo();
        verifyNoInteractions(produtoConverter);
        verifyNoMoreInteractions(produtoRepository);
    }

}
