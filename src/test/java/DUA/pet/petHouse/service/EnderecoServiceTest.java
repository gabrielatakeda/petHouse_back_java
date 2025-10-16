package DUA.pet.petHouse.service;

import DUA.pet.petHouse.model.EnderecoModel;
import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.repository.EnderecoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnderecoServiceTest {

    @InjectMocks
    EnderecoService service;

    @Mock
    EnderecoRepository repository;

    EnderecoModel endereco;

    @BeforeEach
    void setUp (){
        endereco = new EnderecoModel();
        endereco.setId(1L);
        endereco.setEstado("Parana");
        endereco.setCidade("Foz do Iguacu");
        endereco.setBairro("Campos do Iguacu");
        endereco.setLogradouro("Rua Ipanema");
        endereco.setNumero(459);
        endereco.setCep("85857-600");
        endereco.setUsuario(new UsuarioModel());
    }

    @Test
    @DisplayName("Teste função findAll EnderecoService")
    void findAllTest () {
        when(repository.findAll()).thenReturn(List.of(endereco));

        var response = service.findAll();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.size() > 0);
    }

    @Test
    @DisplayName("Teste função findById EnderecoService")
    void findByIdTest () {
        when(repository.findById(1L)).thenReturn(Optional.of(endereco));

        var response = service.findById(1L);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getId());
    }

    @Test
    @DisplayName("Teste função save EnderecoService")
    void saveTest () {
        when(repository.save(any(EnderecoModel.class))).thenReturn(endereco);

        var novoEndereco = new EnderecoModel();
        novoEndereco.setEstado("Parana");
        novoEndereco.setCidade("Foz do Iguacu");
        novoEndereco.setBairro("Campos do Iguacu");
        novoEndereco.setLogradouro("Rua Ipanema");
        novoEndereco.setNumero(459);
        novoEndereco.setCep("85857-600");
        novoEndereco.setUsuario(new UsuarioModel());

        var response = service.save(novoEndereco);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(novoEndereco.getCep(), "85857-600");
    }

    @Test
    @DisplayName("Teste função update EnderecoService")
    void updateTest () {
        when(repository.findById(1L)).thenReturn(Optional.of(endereco));
        when(repository.save(any(EnderecoModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EnderecoModel atualizado = new EnderecoModel();
        atualizado.setId(1L);
        atualizado.setEstado("Parana");
        atualizado.setCidade("Curitiba");
        atualizado.setBairro("Centro");
        atualizado.setLogradouro("Rua das Flores");
        atualizado.setNumero(123);
        atualizado.setCep("80000-000");
        atualizado.setUsuario(endereco.getUsuario());

        EnderecoModel response = service.update(1L, atualizado);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Curitiba", response.getCidade());
    }

    @Test
    @DisplayName("Teste função delete EnderecoService")
    void deleteTest() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L); // chamar delete real
        verify(repository, times(1)).deleteById(1L);
    }

}
