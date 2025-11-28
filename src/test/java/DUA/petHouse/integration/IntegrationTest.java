package DUA.petHouse.integration;

import DUA.pet.petHouse.enums.Role;
import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.model.EnderecoModel;
import DUA.pet.petHouse.repository.UsuarioRepository;
import DUA.pet.petHouse.repository.EnderecoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    private UsuarioModel usuario;
    private EnderecoModel endereco;

    @BeforeEach
    void setup() {
        usuario = new UsuarioModel();
        usuario.setNome("Maria Souza");
        usuario.setEmail("maria@example.com");
        usuario.setUser("mariaUser");
        usuario.setSenha("Jl99766590@");
        usuario.setCpf("12345678900");
        usuario.setRole(Role.ADMIN);
        usuario.setDataNascimento(LocalDate.now());

        endereco = new EnderecoModel();
        endereco.setLogradouro("Rua das Flores");
        endereco.setNumero(100);
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("85857600");
        endereco.setBairro("Centro");
    }

    @Test
    @DisplayName("Deve salvar usuário e endereço e buscar por ID")
    void deveSalvarUsuarioEEndereco() throws Exception {
        // Cria usuário
        String usuarioJson = mapper.writeValueAsString(usuario);

        var usuarioResponse = mockMvc.perform(post("/usuarios/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Maria Souza"))
                .andReturn();

        UsuarioModel usuarioSalvo = mapper.readValue(
                usuarioResponse.getResponse().getContentAsString(),
                UsuarioModel.class
        );

        assertThat(usuarioSalvo.getSenha()).isNotEqualTo("123456"); // senha criptografada

        // Cria endereço
        endereco.setUsuario(usuarioSalvo); // só se existir relacionamento
        String enderecoJson = mapper.writeValueAsString(endereco);

        var enderecoResponse = mockMvc.perform(post("/enderecos/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enderecoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.logradouro").value("Rua das Flores"))
                .andReturn();

        EnderecoModel enderecoSalvo = mapper.readValue(
                enderecoResponse.getResponse().getContentAsString(),
                EnderecoModel.class
        );

        // Busca usuário e endereço por ID
        mockMvc.perform(get("/usuarios/findById/" + usuarioSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Souza"));

        mockMvc.perform(get("/enderecos/findById/" + enderecoSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.logradouro").value("Rua das Flores"));
    }

    @Test
    @DisplayName("Deve autenticar usuário com senha criptografada")
    void deveLogarUsuario() throws Exception {
        String usuarioJson = mapper.writeValueAsString(usuario);
        mockMvc.perform(post("/usuarios/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isCreated());

        // Login com email e senha
        String loginJson = """
                    {
                      "usuarioLogin": "maria@example.com",
                    }
                """;

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("maria@example.com"));
    }

    @Test
    @DisplayName("Deve deletar usuário e endereço")
    void deveDeletarUsuarioEEndereco() throws Exception {
        UsuarioModel u = usuarioRepository.save(usuario);
        EnderecoModel e = enderecoRepository.save(endereco);

        mockMvc.perform(delete("/usuarios/delete/" + u.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/enderecos/delete/" + e.getId()))
                .andExpect(status().isNoContent());
    }
}