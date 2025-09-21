package com.example.iot.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario_tb")
public class UsuarioModel {

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
    private String usuario;

//    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
//    @Valid
//    private List<EnderecoModel> enderecos;



}
