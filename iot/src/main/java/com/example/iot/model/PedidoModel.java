package com.example.iot.model;

import com.example.iot.enums.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido_tb")
public class PedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private UsuarioModel cliente;

    @NotNull(message = "Campo obrigatorio")
    private LocalDateTime dataPedido;

    @NotBlank(message = "Campo obrigatorio")
    private Double total;

    /*
    @OneToMany(mappedBy = "pedido_tb", cascade = CascadeType.ALL)
    private PagamentoModel pagamento;
    */

    @OneToMany(mappedBy = "pedido_tb", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    @OneToMany(mappedBy = "pedido_tb", cascade = CascadeType.ALL)
    private EnderecoModel endereco;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Campo obrigatorio")
    private StatusPedido statusPedido;


}
