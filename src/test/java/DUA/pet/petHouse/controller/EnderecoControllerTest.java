package DUA.pet.petHouse.controller;

import DUA.pet.petHouse.model.EnderecoModel;
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
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EnderecoControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    EnderecoModel endereco;
    UsuarioModel usuario;

    @BeforeEach
    void setUp() {
        pagamentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        enderecoRepository.deleteAll();
        usuarioRepository.deleteAll();


        usuario = new UsuarioModel();
        usuario.setNome("João");
        usuario.setEmail("joao@test.com");
        usuario.setSenha("Senha@123");
        usuario.setUser("joaouser");
        usuario.setCPF("12345678901");
        usuario.setDataNascimento(LocalDate.of(2000, 1, 1));
        usuario = usuarioRepository.save(usuario);

        endereco = new EnderecoModel();
        endereco.setLogradouro("Rua A");
        endereco.setNumero(10);
        endereco.setBairro("Centro");
        endereco.setCidade("Cascavel");
        endereco.setEstado("PR");
        endereco.setCep("85800000");
        endereco.setUsuario(usuario);
        endereco = enderecoRepository.save(endereco);
    }

    @Test
    @DisplayName("Teste findAll Enderecos")
    void findAllTest() {
        ResponseEntity<EnderecoModel[]> response =
                restTemplate.getForEntity("/enderecos/findAll", EnderecoModel[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().length > 0);
    }

    @Test
    @DisplayName("Teste findById existente")
    void findById() {
        ResponseEntity<EnderecoModel> response =
                restTemplate.getForEntity("/enderecos/findById/" + endereco.getId(), EnderecoModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Teste findById inexistente (erro 400)")
    void findByIdBadRequest() {
        ResponseEntity<EnderecoModel> response =
                restTemplate.getForEntity("/enderecos/findById/9999", EnderecoModel.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Teste save Endereco")
    void saveEndereco() {
        EnderecoModel novo = new EnderecoModel();
        novo.setLogradouro("Rua Nova");
        novo.setNumero(123);
        novo.setBairro("Bairro Novo");
        novo.setCidade("Curitiba");
        novo.setEstado("PR");
        novo.setCep("80000000");
        novo.setUsuario(usuario);

        ResponseEntity<EnderecoModel> response =
                restTemplate.postForEntity("/enderecos/save", novo, EnderecoModel.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Teste delete Endereco")
    void deleteEndereco() {
        ResponseEntity<Void> response =
                restTemplate.exchange("/enderecos/delete/" + endereco.getId(), HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
