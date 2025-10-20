package DUA.pet.petHouse.controller;

import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.repository.EnderecoRepository;
import DUA.pet.petHouse.repository.PagamentoRepository;
import DUA.pet.petHouse.repository.PedidoRepository;
import DUA.pet.petHouse.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UsuarioControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

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
}
