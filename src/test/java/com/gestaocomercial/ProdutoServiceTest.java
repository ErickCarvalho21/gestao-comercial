package com.gestaocomercial;

import com.gestaocomercial.produto.Converter.ProdutoConverter;
import com.gestaocomercial.produto.repository.ProdutoRepository;
import com.gestaocomercial.produto.service.ProdutoService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoConverter produtoConverter;

}
