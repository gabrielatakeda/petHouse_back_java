package DUA.petHouse.controller;

import DUA.petHouse.model.EnderecoModel;
import DUA.petHouse.model.UsuarioModel;
import DUA.petHouse.repository.EnderecoRepository;
import DUA.petHouse.repository.PagamentoRepository;
import DUA.petHouse.repository.PedidoRepository;
import DUA.petHouse.repository.UsuarioRepository;
import DUA.petHouse.service.EnderecoService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EnderecoControllerTest {

    @Mock
    EnderecoService enderecoService;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Mock
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
        usuario.setCpf("12345678901");
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

    @Test
    @DisplayName("Teste update Endereco existente")
    void updateEndereco() {
        // Novo endereço com dados atualizados
        EnderecoModel atualizado = new EnderecoModel();
        atualizado.setLogradouro("Rua Atualizada");
        atualizado.setNumero(999);
        atualizado.setBairro("Bairro Atualizado");
        atualizado.setCidade("Curitiba");
        atualizado.setEstado("PR");
        atualizado.setCep("85000000");
        atualizado.setUsuario(usuario);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EnderecoModel> requestEntity = new HttpEntity<>(atualizado, headers);

        ResponseEntity<EnderecoModel> response = restTemplate
                .exchange(
                "/enderecos/update/" + endereco.getId(),
                HttpMethod.PUT,
                requestEntity,
                EnderecoModel.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Rua Atualizada", response.getBody().getLogradouro());
    }

    @Test
    @DisplayName("save - exceção lançada")
    void saveEnderecoException() {
        var controllerToTest = new EnderecoController(enderecoService, enderecoRepository);

        EnderecoModel novoEndereco = new EnderecoModel();
        novoEndereco.setLogradouro("Rua Nova");
        novoEndereco.setNumero(123);
        novoEndereco.setBairro("Bairro Novo");
        novoEndereco.setCidade("Curitiba");
        novoEndereco.setEstado("PR");
        novoEndereco.setCep("80000000");
        novoEndereco.setUsuario(usuario);

        when(enderecoService.save(any(EnderecoModel.class)))
                .thenThrow(new RuntimeException("Erro ao salvar"));

        ResponseEntity<EnderecoModel> response = controllerToTest.save(novoEndereco);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("delete - endereço inexistente")
    void deleteEnderecoNotFound() {
        var controllerToTest = new EnderecoController(enderecoService, enderecoRepository);

        when(enderecoRepository.existsById(999L)).thenReturn(false);

        ResponseEntity<Void> response = controllerToTest.delete(999L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    @Test
    @DisplayName("delete - exceção lançada pelo service")
    void deleteEnderecoServiceException() {
        var controllerToTest = new EnderecoController(enderecoService, enderecoRepository);

        when(enderecoRepository.existsById(999L)).thenReturn(true);

        doThrow(new RuntimeException("Erro ao deletar")).when(enderecoService).delete(999L);

        ResponseEntity<Void> response = controllerToTest.delete(999L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    @DisplayName("update - exceção lançada")
    void updateEnderecoException() {
        var controllerToTest = new EnderecoController(enderecoService, enderecoRepository);

        EnderecoModel enderecoAtualizado = new EnderecoModel();
        enderecoAtualizado.setLogradouro("Rua Atualizada");
        enderecoAtualizado.setNumero(999);
        enderecoAtualizado.setBairro("Bairro Atualizado");
        enderecoAtualizado.setCidade("Curitiba");
        enderecoAtualizado.setEstado("PR");
        enderecoAtualizado.setCep("85000000");
        enderecoAtualizado.setUsuario(usuario);

        when(enderecoService.update(anyLong(), any(EnderecoModel.class)))
                .thenThrow(new RuntimeException("Erro ao atualizar"));

        ResponseEntity<EnderecoModel> response = controllerToTest.update(999L, enderecoAtualizado);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("findAll - exceção lançada")
    void findAllEnderecoException() {
        var controllerToTest = new EnderecoController(enderecoService, enderecoRepository);

        when(enderecoService.findAll()).thenThrow(new RuntimeException("Erro ao listar enderecos"));

        ResponseEntity<List<EnderecoModel>> response = controllerToTest.findAll();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }


}
