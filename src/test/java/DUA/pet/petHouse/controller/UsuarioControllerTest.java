package DUA.pet.petHouse.controller;

import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.repository.EnderecoRepository;
import DUA.pet.petHouse.repository.PagamentoRepository;
import DUA.pet.petHouse.repository.PedidoRepository;
import DUA.pet.petHouse.repository.UsuarioRepository;
import DUA.pet.petHouse.service.UsuarioService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UsuarioControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Mock
    UsuarioService usuarioService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    UsuarioModel usuario;

    @BeforeEach
    void setUp() {
        pagamentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        enderecoRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuario = new UsuarioModel();
        usuario.setNome("Maria");
        usuario.setEmail("maria@test.com");
        usuario.setSenha("Senha@123");
        usuario.setUser("mariauser");
        usuario.setCPF("11122233344");
        usuario.setDataNascimento(LocalDate.of(1995, 5, 20));
        usuario.setEnderecos(new ArrayList<>());
        usuario = usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("findAll Usuarios")
    void findAll() {
        ResponseEntity<UsuarioModel[]> response =
                restTemplate.getForEntity("/usuarios/findAll", UsuarioModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("findById existente")
    void findById() {
        ResponseEntity<UsuarioModel> response =
                restTemplate.getForEntity("/usuarios/findById/" + usuario.getId(), UsuarioModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("findById inexistente")
    void findByIdBadRequest() {
        ResponseEntity<UsuarioModel> response =
                restTemplate.getForEntity("/usuarios/findById/9999", UsuarioModel.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("save Usuario")
    void save() {
        UsuarioModel novo = new UsuarioModel();
        novo.setNome("Carlos");
        novo.setEmail("carlos@test.com");
        novo.setSenha("Senha@123");
        novo.setUser("carlosuser");
        novo.setCPF("12345678910");
        novo.setDataNascimento(LocalDate.of(2001, 8, 15));
        novo.setEnderecos(new ArrayList<>());

        ResponseEntity<UsuarioModel> response =
                restTemplate.postForEntity("/usuarios/save", novo, UsuarioModel.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("delete Usuario")
    void delete() {
        ResponseEntity<Void> response = restTemplate
                .exchange("/usuarios/delete/" + usuario.getId(), HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("save - exceção lançada")
    void saveUsuarioException() {
        var controllerToTest = new UsuarioController(usuarioService);
        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setNome("Carlos");
        novoUsuario.setEmail("carlos@test.com");
        novoUsuario.setSenha("Senha@123");
        novoUsuario.setUser("carlosuser");
        novoUsuario.setCPF("12345678910");
        novoUsuario.setDataNascimento(LocalDate.of(2001, 8, 15));
        novoUsuario.setEnderecos(new ArrayList<>());

        when(usuarioService.save(any(UsuarioModel.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        ResponseEntity<UsuarioModel> response = controllerToTest.save(novoUsuario);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("delete - exceção lançada")
    void deleteUsuarioException() {
        var controllerToTest = new UsuarioController(usuarioService);
        doThrow(new RuntimeException("Erro ao deletar")).when(usuarioService).delete(anyLong());

        ResponseEntity<Void> response = controllerToTest.delete(999L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("update - exceção lançada")
    void updateUsuarioException() {
        var controllerToTest = new UsuarioController(usuarioService);
        UsuarioModel usuarioAtualizado = new UsuarioModel();
        usuarioAtualizado.setNome("Maria Atualizada");
        usuarioAtualizado.setEmail("maria.atualizada@test.com");
        usuarioAtualizado.setSenha("Senha@123");
        usuarioAtualizado.setUser("mariauser");
        usuarioAtualizado.setCPF("11122233344");
        usuarioAtualizado.setDataNascimento(LocalDate.of(1995, 5, 20));
        usuarioAtualizado.setEnderecos(new ArrayList<>());

        when(usuarioService.update(anyLong(), any(UsuarioModel.class)))
                .thenThrow(new RuntimeException("Erro ao atualizar"));

        ResponseEntity<UsuarioModel> response = controllerToTest.update(999L, usuarioAtualizado);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("findAll - exceção lançada")
    void findAllUsuarioException() {
        var controllerToTest = new UsuarioController(usuarioService);
        when(usuarioService.findAll()).thenThrow(new RuntimeException("Erro ao listar usuarios"));

        ResponseEntity<List<UsuarioModel>> response = controllerToTest.findAll();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }


}
