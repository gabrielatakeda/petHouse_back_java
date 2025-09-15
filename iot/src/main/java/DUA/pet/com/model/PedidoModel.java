package DUA.pet.com.model;

import DUA.pet.com.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

//    @OneToMany(mappedBy = "pedido_tb", cascade = CascadeType.ALL)
//    private PagamentoModel pagamento;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference
    private PagamentoModel pagamento;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ItemPedido> itens;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private EnderecoModel endereco;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Campo obrigatorio")
    private StatusPedido statusPedido;


}
