package com.example.iot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity //Marca como uma entidade no banco de dados
@Table(name = "produto_tabela") //Marca com esse nome entre os parênteses no banco
@Getter //Gera automaticamente
@Setter
@ToString
public class ProdutoModel {
    //Atributos
    @Id //marca como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Gera automaticamente o valor do id
    @Column(name = "id_produto") //Marca a coluna com esse nome
    private Long id;

    @NotBlank(message = "O nome deve ser valido")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "O produto deve conter uma descrição")
    private String descricao;

    private Double precoVenda;
    private Integer quantidade;

    @ManyToOne
    private Categoria categoria;
}
