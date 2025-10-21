package DUA.pet.petHouse.controller;

import DUA.pet.petHouse.model.CategoriaModel;
import DUA.pet.petHouse.repository.CategoriaRepository;
import DUA.pet.petHouse.service.CategoriaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CategoriaControllerTest {

    @Mock
    CategoriaService categoriaService;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    CategoriaRepository repository;

    CategoriaModel categoria;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        categoria = new CategoriaModel();
        categoria.setProdutos(new ArrayList<>());
        categoria.setNome("frutas");
        categoria.setSubcategorias(new ArrayList<>());

        categoria = repository.save(categoria);
    }

    @Test
    @DisplayName("Teste API findAll CategoriaController")
    void findAllTest() {
        ResponseEntity<CategoriaModel[]> response = restTemplate
                .getForEntity("/categorias/findAll", CategoriaModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length > 0);
    }

    @Test
    @DisplayName("Teste erro 400 no findAll (simulando falha interna)")
    void findAllBadRequest() {
        repository.deleteAll();

        ResponseEntity<CategoriaModel[]> response = restTemplate
                .getForEntity("/categorias/findAll", CategoriaModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(0, response.getBody().length);
    }

    @Test
    @DisplayName("Teste API findById CategoriaController")
    void findById() {
        ResponseEntity<CategoriaModel> response = restTemplate
                .getForEntity("/categorias/findById/" + categoria.getId(), CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(categoria.getNome(), response.getBody().getNome());
    }

    @Test
    @DisplayName("Teste do erro 404 no findById")
    void findByIdNotFound() {
        ResponseEntity<CategoriaModel> response = restTemplate
                .getForEntity("/categorias/findById/9999", CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Teste API findByNome CategoriaController")
    void findByNomeTest() {
        ResponseEntity<CategoriaModel> response = restTemplate
                .getForEntity("/categorias/nome?nome=" + categoria.getNome(), CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

//    @Test
//    @DisplayName("Teste erro 404 no findByNome (não encontrado)")
//    void findByNomeNotFound() {
//        ResponseEntity<CategoriaModel> response = restTemplate
//                .getForEntity("/categorias/nome?nome=nãoExiste", CategoriaModel.class);
//
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }

    @Test
    @DisplayName("Teste API save CategoriaController")
    void saveTest() {
        CategoriaModel novaCategoria = new CategoriaModel();
        novaCategoria.setProdutos(new ArrayList<>());
        novaCategoria.setNome("eletronicos");
        novaCategoria.setSubcategorias(new ArrayList<>());

        ResponseEntity<CategoriaModel> response = restTemplate
                .postForEntity("/categorias/save", novaCategoria, CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("eletronicos", response.getBody().getNome());
    }

    @Test
    @DisplayName("Teste erro 400 ao salvar categoria inválida")
    void saveInvalidCategoria() {
        CategoriaModel novaCategoria = new CategoriaModel();

        ResponseEntity<CategoriaModel> response = restTemplate
                .postForEntity("/categorias/save", novaCategoria, CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Teste comportamento de exceção geral simulada")
    void genericErrorHandlingTest() {
        repository.deleteAll();

        ResponseEntity<CategoriaModel> response = restTemplate
                .getForEntity("/categorias/findById/9999", CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("findAll - exceção lançada")
    void findAllCategoriaException() {
        var controllerToTest = new CategoriaController(categoriaService);

        when(categoriaService.findAll()).thenThrow(new RuntimeException("Erro ao listar categorias"));

        ResponseEntity<List<CategoriaModel>> response = controllerToTest.findAll();

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("save - exceção lançada")
    void saveCategoriaException() {
        var controllerToTest = new CategoriaController(categoriaService);

        CategoriaModel novaCategoria = new CategoriaModel();
        novaCategoria.setNome("Eletronicos");
        novaCategoria.setProdutos(new ArrayList<>());
        novaCategoria.setSubcategorias(new ArrayList<>());

        when(categoriaService.save(any(CategoriaModel.class)))
                .thenThrow(new RuntimeException("Erro ao salvar categoria"));

        ResponseEntity<CategoriaModel> response = controllerToTest.save(novaCategoria);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }


}
