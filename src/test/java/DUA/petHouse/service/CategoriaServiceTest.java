package DUA.petHouse.service;

import DUA.petHouse.model.CategoriaModel;
import DUA.petHouse.repository.CategoriaRepository;
import DUA.petHouse.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService service;

    @Mock
    private CategoriaRepository repository;

    @Mock
    private ProdutoRepository produtoRepository;

    private CategoriaModel categoria;

    @BeforeEach
    void setUp() {
        categoria = new CategoriaModel();
        categoria.setId(1L);
        categoria.setNome("Informatica");
        categoria.setSubcategorias(new ArrayList<>());
        categoria.setProdutos(new ArrayList<>());
        categoria.setSlug("Informatica");
    }

    @Test
    @DisplayName("Teste função FindAll CategoriaService")
    void findAllTest() {
        when(repository.findAll()).thenReturn(List.of(categoria));

        var response = service.findAll();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.size() > 0);
    }

    @Test
    @DisplayName("Teste função FindById CategoriaService")
    void findByIdTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        var response = service.findById(1L);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(categoria.getId(), response.get().getId());
    }

    @Test
    @DisplayName("Teste função FindByNome CategoriaService")
    void findByNomeTest() {
        when(repository.findByNome("Informatica")).thenReturn(Optional.of(categoria));

        var response = service.findByNome("Informatica");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(categoria.getNome(), response.get().getNome());
    }

    @Test
    @DisplayName("Teste função Save CategoriaService")
    void saveTest() {
        when(repository.save(any(CategoriaModel.class))).thenReturn(categoria);

        var novaCategoria = new CategoriaModel();
        novaCategoria.setNome("Informatica");
        novaCategoria.setSubcategorias(new ArrayList<>());
        novaCategoria.setProdutos(new ArrayList<>());
        novaCategoria.setSlug("Informatica");

        var response = service.save(novaCategoria);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(novaCategoria.getNome(), response.getNome());
    }
}
