package DUA.petHouse.service;

import DUA.petHouse.bucket.BucketFile;
import DUA.petHouse.bucket.BucketService;
import DUA.petHouse.model.CategoriaModel;
import DUA.petHouse.model.ProdutoModel;
import DUA.petHouse.repository.ProdutoRepository;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @InjectMocks
    ProdutoService service;

    @Mock
    ProdutoRepository repository;

    @Mock
    BucketService bucket;

    ProdutoModel produto;

    @BeforeEach
    void setUp() {
        produto = new ProdutoModel();
        produto.setId(1L);
        produto.setNome("banana");
        produto.setDescricao("banana prata");
        produto.setQuantidade(10);
        produto.setPrecoVenda(5.59);
        produto.setUrlFoto("https://meu-bucket/imagens/banana.png");
        produto.setCategoria(new CategoriaModel());
    }

    @Test
    @DisplayName("Teste função findAll ProdutoService")
    void finAllTest () {
        when(repository.findAll()).thenReturn(List.of(produto));

        var response = service.findAll();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.size() > 0);
    }

    @Test
    @DisplayName("Teste função findById ProdutoService")
    void findByIdTest () {
        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        var response = service.findById(1L);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getNome(), produto.getNome());
    }

    @Test
    @DisplayName("Teste função findByNome ProdutoService")
    void findByNomeTest() {
        when(repository.getByNome("banana")).thenReturn(List.of(produto));

        var response = service.findByNome("banana");

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals("banana", response.get(0).getNome());
    }

    @Test
    @DisplayName("Teste função update ProdutoService - campos nulos (não entra nos ifs)")
    void updateSemCamposParaAtualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(repository.save(any(ProdutoModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProdutoModel atualizado = new ProdutoModel();
        atualizado.setId(1L);
        atualizado.setNome("banana");
        atualizado.setDescricao("banana prata");
        atualizado.setPrecoVenda(null);
        atualizado.setQuantidade(null);
        atualizado.setCategoria(new CategoriaModel());

        ProdutoModel response = service.update(1L, atualizado);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(produto.getPrecoVenda(), response.getPrecoVenda());
        Assertions.assertEquals(produto.getQuantidade(), response.getQuantidade());
        verify(repository, times(1)).save(any(ProdutoModel.class));
    }



    @Test
    @DisplayName("Teste função save ProdutoService com BucketFile mockado")
    void saveTest() {
        BucketFile bucketFile = mock(BucketFile.class);
        when(bucket.upload(bucketFile)).thenReturn("https://meu-bucket/imagens/banana.png");
        when(repository.save(any(ProdutoModel.class))).thenReturn(produto);

        ProdutoModel novoProduto = new ProdutoModel();
        novoProduto.setNome("banana");
        novoProduto.setDescricao("banana prata");
        novoProduto.setQuantidade(10);
        novoProduto.setPrecoVenda(5.59);
        novoProduto.setCategoria(new CategoriaModel());

        ProdutoModel response = service.save(novoProduto, bucketFile);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("banana", response.getNome());
        Assertions.assertEquals("https://meu-bucket/imagens/banana.png", response.getUrlFoto());

        verify(bucket, times(1)).upload(bucketFile);
        verify(repository, times(1)).save(any(ProdutoModel.class));
    }



    @Test
    @DisplayName("Teste função update ProdutoService")
    void updateTest () {
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(repository.save(any(ProdutoModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        var atualizado = new ProdutoModel();
        atualizado.setId(1L);
        atualizado.setNome("banana");
        atualizado.setDescricao("banana prata");
        atualizado.setQuantidade(10);
        atualizado.setPrecoVenda(5.59);
        atualizado.setUrlFoto("https://meu-bucket/imagens/banana.png");
        atualizado.setCategoria(new CategoriaModel());

        var response = service.update(1L,atualizado);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(atualizado.getNome(), response.getNome());
    }

    @Test
    @DisplayName("Teste função deleteById ProdutoService")
    void deleteByIdTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        doNothing().when(repository).delete(produto);

        service.deleteById(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(produto);
    }

}
