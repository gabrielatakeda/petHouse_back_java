package DUA.pet.petHouse.service;

import DUA.pet.petHouse.enums.Role;
import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.repository.UsuarioRepository;
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
public class UsuarioServiceTest {

    @InjectMocks
    UsuarioService service;

    @Mock
    UsuarioRepository repository;

    UsuarioModel usuario;

    @BeforeEach
    void setUp () {
        usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNome("João Luiz Martinazzo");
        usuario.setUser("JLMartinazzo");
        usuario.setCPF("132.218.199-35");
        usuario.setEmail("jl.martinazzo@gmaiil.com");
        usuario.setSenha("jl99766590");
        usuario.setRole(Role.ADMIN);
        usuario.setEnderecos(new ArrayList<>());
    }

    @Test
    @DisplayName("Teste função findAll UsuarioService")
    void findAllTest () {
        when(repository.findAll()).thenReturn(List.of(usuario));

        var response = service.findAll();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.size() > 0);
    }

    @Test
    @DisplayName("Teste função findById UsuarioService")
    void findByIdTest () {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        var response = service.findById(1L);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getUser(), usuario.getUser());
    }

    @Test
    @DisplayName("Teste função save UsuarioService")
    void saveTest () {
        when(repository.save(any(UsuarioModel.class))).thenReturn(usuario);

        UsuarioModel usuarioSave = new UsuarioModel();
        usuarioSave.setId(1L);
        usuarioSave.setNome("João Luiz Martinazzo");
        usuarioSave.setUser("JLMartinazzo");
        usuarioSave.setCPF("132.218.199-35");
        usuarioSave.setEmail("jl.martinazzo@gmaiil.com");
        usuarioSave.setSenha("jl99766590");
        usuarioSave.setRole(Role.ADMIN);
        usuarioSave.setEnderecos(new ArrayList<>());

        var response = service.save(usuarioSave);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(usuarioSave.getCPF(), response.getCPF());
    }

    @Test
    @DisplayName("Teste função update UsuarioService")
    void updateTest () {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.save(any(UsuarioModel.class))).thenReturn(usuario);

        UsuarioModel update = new UsuarioModel();
        update.setId(1L);
        update.setNome("Alceni Jose Martinazzo");
        update.setUser("alceniMartinazzo");
        update.setCPF("857.689.019-49");
        update.setEmail("ni.martinazzo@hotmail.com");
        update.setSenha("91145114");
        update.setRole(Role.USER);
        update.setEnderecos(new ArrayList<>());

        var response = service.update(1L, update);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(update.getUser(), response.getUser());
    }

    @Test
    @DisplayName("Teste função delete UsuarioService")
    void deleteTest () {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);
        verify(repository,times(1)).deleteById(1L);
    }

}