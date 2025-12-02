package DUA.petHouse.model;

import DUA.petHouse.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotNull(message = "Campo obrigatorio")
    private Double total;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonBackReference
    private PagamentoModel pagamento;

    @ManyToMany
    @JoinTable(
            name = "pedido_produto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<ProdutoModel> produtos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private EnderecoModel endereco;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    // campo transitório para guardar a quantidade escolhida pelo cliente
    @Transient
    private Integer quantidadeSelecionada;
}