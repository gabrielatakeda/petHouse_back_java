package DUA.pet.petHouse.controller;

import DUA.pet.petHouse.model.ProdutoModel;
import DUA.pet.petHouse.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProdutoControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ProdutoRepository produtoRepository;

    ProdutoModel produto;

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
}
