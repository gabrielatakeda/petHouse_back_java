package DUA.petHouse.controller;

import DUA.petHouse.model.CategoriaModel;
import DUA.petHouse.repository.CategoriaRepository;
import DUA.petHouse.service.CategoriaService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CategoriaControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CategoriaRepository repository;

    private CategoriaModel categoria;

    @org.mockito.Mock
    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        categoria = new CategoriaModel();
        categoria.setNome("frutas");
        categoria.setSlug("frutas");
        categoria.setProdutos(new ArrayList<>());
        categoria.setSubcategorias(new ArrayList<>());
        categoria = repository.save(categoria);
    }

    // ---------- FIND ALL ----------
    @Test
    @DisplayName("findAll - deve retornar lista de categorias com sucesso")
    void findAllTest() {
        ResponseEntity<CategoriaModel[]> response =
                restTemplate.getForEntity("/categorias/findAll", CategoriaModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length > 0);
    }

    @Test
    @DisplayName("findAll - deve retornar lista vazia quando não há categorias")
    void findAllSemCategorias() {
        repository.deleteAll();

        ResponseEntity<CategoriaModel[]> response =
                restTemplate.getForEntity("/categorias/findAll", CategoriaModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(0, response.getBody().length);
    }

    @Test
    @DisplayName("findAll - exceção lançada no service retorna 404")
    void findAllExceptionTest() {
        var controller = new CategoriaController(categoriaService);
        when(categoriaService.findAll()).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<List<CategoriaModel>> response = controller.findAll();

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    // ---------- FIND BY ID ----------
    @Test
    @DisplayName("findById - deve retornar categoria existente")
    void findById() {
        ResponseEntity<CategoriaModel> response =
                restTemplate.getForEntity("/categorias/findById/" + categoria.getId(), CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(categoria.getNome(), response.getBody().getNome());
    }

    @Test
    @DisplayName("findById - categoria inexistente retorna 404")
    void findByIdNotFound() {
        ResponseEntity<CategoriaModel> response =
                restTemplate.getForEntity("/categorias/findById/99999", CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ---------- FIND BY NOME ----------
    @Test
    @DisplayName("findByNome - deve retornar categoria existente")
    void findByNome() {
        ResponseEntity<CategoriaModel> response =
                restTemplate.getForEntity("/categorias/nome/" + categoria.getNome(), CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("findByNome - deve retornar 204 quando categoria não existe")
    void findByNomeNoContent() {
        ResponseEntity<CategoriaModel> response =
                restTemplate.getForEntity("/categorias/nome/inexistente", CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("findByNome - exceção retorna BAD_REQUEST")
    void findByNomeException() {
        var controller = new CategoriaController(categoriaService);
        when(categoriaService.findByNome("erro")).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Optional<CategoriaModel>> response = controller.findByNome("erro");

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    // ---------- SAVE ----------
    @Test
    @DisplayName("save - deve criar categoria com sucesso")
    void saveCategoria() {
        CategoriaModel novaCategoria = new CategoriaModel();
        novaCategoria.setNome("eletronicos");
        novaCategoria.setSlug("eletronicos");
        novaCategoria.setProdutos(new ArrayList<>());
        novaCategoria.setSubcategorias(new ArrayList<>());

        ResponseEntity<CategoriaModel> response =
                restTemplate.postForEntity("/categorias/save", novaCategoria, CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("eletronicos", response.getBody().getNome());
    }

    @Test
    @DisplayName("save - deve retornar BAD_REQUEST quando inválido")
    void saveCategoriaInvalida() {
        CategoriaModel novaCategoria = new CategoriaModel();

        ResponseEntity<CategoriaModel> response =
                restTemplate.postForEntity("/categorias/save", novaCategoria, CategoriaModel.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("save - exceção lançada retorna BAD_REQUEST")
    void saveCategoriaException() {
        var controller = new CategoriaController(categoriaService);
        CategoriaModel model = new CategoriaModel();
        model.setNome("Teste");

        when(categoriaService.save(any())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<CategoriaModel> response = controller.save(model);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // ---------- CRIAR SUBCATEGORIA ----------
    @Test
    @DisplayName("criarSubcategoria - deve retornar categoria criada")
    void criarSubcategoria() {
        var controller = new CategoriaController(categoriaService);

        CategoriaModel sub = new CategoriaModel();
        sub.setNome("Subcategoria");

        CategoriaModel esperado = new CategoriaModel();
        esperado.setNome("Subcategoria");

        when(categoriaService.criarSubcategoriaPorSlug("pai", sub)).thenReturn(esperado);

        ResponseEntity<CategoriaModel> response = controller.criarSubcategoria("pai", sub);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Subcategoria", response.getBody().getNome());
    }

    @Test
    @DisplayName("findCategoriasPai - deve retornar categorias sem pai")
    void findCategoriasPai() {
        CategoriaModel filho = new CategoriaModel();
        filho.setNome("Filho");
        filho.setCategoriaPai(categoria);
        filho.setSlug("filho");
        repository.save(filho);

        ResponseEntity<CategoriaModel[]> response =
                restTemplate.getForEntity("/categorias/pai", CategoriaModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(Arrays.stream(response.getBody())
                .allMatch(c -> c.getCategoriaPai() == null));
    }

    @Test
    @DisplayName("findCategoriasPai - exceção retorna BAD_REQUEST")
    void findCategoriasPaiException() {
        var controller = new CategoriaController(categoriaService);
        when(categoriaService.findAll()).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<List<CategoriaModel>> response = controller.findCategoriasPai();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Teste API findSubcategorias CategoriaController")
    void findSubcategorias() {
        // Cria categoria pai
        CategoriaModel pai = new CategoriaModel();
        pai.setNome("Eletrônicos");
        pai.setSlug("eletronicos");
        pai.setProdutos(new ArrayList<>());
        pai.setSubcategorias(new ArrayList<>());
        pai = repository.save(pai);

        // Cria subcategoria
        CategoriaModel sub = new CategoriaModel();
        sub.setNome("Celulares");
        sub.setSlug("celulares");
        sub.setCategoriaPai(pai);
        sub.setProdutos(new ArrayList<>());
        sub.setSubcategorias(new ArrayList<>());
        repository.save(sub);

        ResponseEntity<CategoriaModel[]> response = restTemplate
                .getForEntity("/categorias/" + pai.getId() + "/subcategorias", CategoriaModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length > 0);
        Assertions.assertEquals("Celulares", response.getBody()[0].getNome());
    }

    @Test
    @DisplayName("findSubcategorias - categoria inexistente retorna lista vazia")
    void findSubcategoriasCategoriaInexistente() {
        ResponseEntity<CategoriaModel[]> response =
                restTemplate.getForEntity("/categorias/9999/subcategorias", CategoriaModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(0, response.getBody().length);
    }

    @Test
    @DisplayName("findSubcategorias - exceção retorna BAD_REQUEST")
    void findSubcategoriasException() {
        var controller = new CategoriaController(categoriaService);
        when(categoriaService.findById(anyLong())).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<List<CategoriaModel>> response = controller.findSubcategorias(1L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
