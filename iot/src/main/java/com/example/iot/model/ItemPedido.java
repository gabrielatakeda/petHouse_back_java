package com.example.iot.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Unsigned;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo obrigatorio")
    private int qtd;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private PedidoModel pedido;


    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoModel produto;

}
