package DUA.petHouse.controller;

import DUA.petHouse.bucket.BucketFile;
import DUA.petHouse.model.ProdutoModel;
import DUA.petHouse.repository.ProdutoRepository;
import DUA.petHouse.service.ProdutoService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProdutoControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ProdutoRepository produtoRepository;

    ProdutoModel produto;

    @Mock
    ProdutoService produtoService;

    @Autowired
    ProdutoController produtoController;

    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();

        produto = new ProdutoModel();
        produto.setNome("Banana");
        produto.setDescricao("Banana nanica");
        produto.setPrecoVenda(2.5);
        produto.setQuantidade(10);
        produto.setUrlFoto("foto.jpg");
        produto = produtoRepository.save(produto);

    }

    @Test
    @DisplayName("findAll Produtos")
    void findAll() {
        ResponseEntity<ProdutoModel[]> response =
                restTemplate.getForEntity("/produtos/findAll", ProdutoModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("findById existente")
    void findById() {
        ResponseEntity<ProdutoModel> response =
                restTemplate.getForEntity("/produtos/findById/" + produto.getId(), ProdutoModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("findById inexistente")
    void findByIdNotFound() {
        ResponseEntity<ProdutoModel> response =
                restTemplate.getForEntity("/produtos/findById/9999", ProdutoModel.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("update Produto")
    void updateProduto() {
        produto.setNome("Banana Prata");

        HttpEntity<ProdutoModel> request = new HttpEntity<>(produto);
        ResponseEntity<ProdutoModel> response = restTemplate
                .exchange("/produtos/update/" + produto.getId(), HttpMethod.PUT, request, ProdutoModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("delete Produto")
    void deleteProduto() {
        ResponseEntity<Void> response = restTemplate
                .exchange("/produtos/deleteById/" + produto.getId(), HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Save Produto")
    void saveProduto () {

        var controllerToTeste = new ProdutoController(produtoService);
        ProdutoModel produtoNovo = new ProdutoModel();
        produtoNovo.setNome("Molho de tomate");
        produtoNovo.setDescricao("Molho de tomate Elefante 250ml");
        produtoNovo.setPrecoVenda(5.0);
        produtoNovo.setQuantidade(25);
        produtoNovo.setUrlFoto("foto.jpg");

        MultipartFile mockFile = new MockMultipartFile(
                "file",                       // nome do campo
                "imagem.jpeg",                // nome do arquivo
                MediaType.IMAGE_JPEG_VALUE,   // tipo do conteúdo
                "conteudo qualquer".getBytes() // conteúdo binário
        );

        when(produtoService.save(any(ProdutoModel.class), any(BucketFile.class))).thenReturn(produtoNovo);

        var response = controllerToTeste.save(produtoNovo, mockFile);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Molho de tomate", response.getBody().getNome());
    }

    @Test
    @DisplayName("findByNome produto")
    void findByNomeProduto () {
        ResponseEntity<ProdutoModel[]> response = restTemplate
                .getForEntity("/produtos/nome/" + produto.getNome(), ProdutoModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length > 0);
    }

    @Test
    @DisplayName("findByNome produto - exceção BAD_REQUEST")
    void findByNomeProdutoBadRequest() {
        var controllerToTest = new ProdutoController(produtoService);

        when(produtoService.findByNome("erro"))
                .thenThrow(new RuntimeException("erro simulado"));

        ResponseEntity<List<ProdutoModel>> response = controllerToTest.findByNome("erro");

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    @DisplayName("findByNome produto - exeção NO_CONTENT")
    void findByNomeProdutoExeption () {
        ResponseEntity<ProdutoModel[]> response = restTemplate
                .getForEntity("/produtos/nome/inexistente" , ProdutoModel[].class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("findAll - exceção lançada")
    void findAllException() {
        var controllerToTest = new ProdutoController(produtoService);
        when(produtoService.findAll()).thenThrow(new RuntimeException("Erro ao listar produtos"));

        ResponseEntity<List<ProdutoModel>> response = controllerToTest.findAll();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("save - exceção lançada")
    void saveProdutoException() {
        var controllerToTest = new ProdutoController(produtoService);

        ProdutoModel produtoNovo = new ProdutoModel();
        produtoNovo.setNome("Molho de tomate");
        produtoNovo.setDescricao("Molho de tomate Elefante 250ml");
        produtoNovo.setPrecoVenda(5.0);
        produtoNovo.setQuantidade(25);
        produtoNovo.setUrlFoto("foto.jpg");

        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "imagem.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "conteudo qualquer".getBytes()
        );

        when(produtoService.save(any(ProdutoModel.class), any(BucketFile.class)))
                .thenThrow(new RuntimeException("Erro ao salvar produto"));

        Assertions.assertThrows(RuntimeException.class, () -> {
            controllerToTest.save(produtoNovo, mockFile);
        });
    }

    @Test
    @DisplayName("deleteById - exceção lançada")
    void deleteByIdException() {
        var controllerToTest = new ProdutoController(produtoService);
        doThrow(new RuntimeException("Erro ao deletar")).when(produtoService).deleteById(anyLong());

        ResponseEntity<Void> response = controllerToTest.deleteById(999L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("update - exceção lançada")
    void updateProdutoException() {
        var controllerToTest = new ProdutoController(produtoService);

        ProdutoModel produtoAtualizado = new ProdutoModel();
        produtoAtualizado.setNome("Banana Prata");

        when(produtoService.update(anyLong(), any(ProdutoModel.class)))
                .thenThrow(new RuntimeException("Erro ao atualizar"));

        ResponseEntity<ProdutoModel> response = controllerToTest.update(999L, produtoAtualizado);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("update - exceção (NOT_FOUND)")
    void updateNotFound() {
        var controllerToTest = new ProdutoController(produtoService);
        ProdutoModel produtoAtualizado = new ProdutoModel();
        produtoAtualizado.setNome("Produto inexistente");

        when(produtoService.update(anyLong(), any(ProdutoModel.class))).thenReturn(null);

        ResponseEntity<ProdutoModel> response = controllerToTest.update(999L, produtoAtualizado);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }


}
