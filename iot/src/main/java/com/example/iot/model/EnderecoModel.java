package com.example.iot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "endereco_tb")
public class EnderecoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo obrigatorio")
    private String logradouro;

    @NotNull(message = "Campo obrigatorio")
    private int numero;

    @NotBlank(message = "Campo obrigatorio")
    private String bairro;

    @NotBlank(message = "Campo obrigatorio")
    private String cidade;

    @NotBlank(message = "Campo obrigatorio")
    private String estado;

    @NotBlank(message = "Campo obrigatorio")
    @Size(min = 8, max = 8, message = "Minimo e Maximo 8 caracteres")
    //no caso ele so vai receber os 8 numeros presentes no CEP, sem o caracter especial
    private String cep;

    @ManyToOne()
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;



}
