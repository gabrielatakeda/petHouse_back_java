package DUA.pet.petHouse.model;

import DUA.pet.petHouse.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario_tb")
public class UsuarioModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @NotBlank(message = "Campo obrigatorio")
    private String nome;

    @Email(message = "Formato do email invalido")
    @NotBlank(message = "Campo obrigatorio")
    private String email;

    @NotBlank(message = "Campo obrigatorio")
    @Size(min = 8, message = "Minimo de 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
            message = "Senha deve ter letras maiúsculas, minúsculas, números e caracteres especiais"
    )
    private String senha;

    @NotBlank(message = "Campo obrigatorio")
    private String user;

    @NotBlank(message = "Campo obrigatorio")
    @Pattern(
            regexp = "^(?:\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}|\\d{11})$",
            message = "CPF deve estar no formato 000.000.000-00 ou 00000000000"
    )
    private String cpf;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

//    @Past(message = "Data de nascimento deve ser no passado")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<EnderecoModel> enderecos;

    @PrePersist
    private void setUp(){
        if (this.role == null) {
            this.role = Role.USER;
        }
    }

    //USER DETAILS

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.user;
    }


}
