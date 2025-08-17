package com.example.iot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    private String senha;

    @NotBlank(message = "Campo obrigatorio")
    private String usuario;

    @OneToMany(mappedBy = "usuario")
    private List<EnderecoModel> enderecos;



}
