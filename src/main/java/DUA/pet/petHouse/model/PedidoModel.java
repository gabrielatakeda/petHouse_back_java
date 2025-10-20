package DUA.pet.petHouse.model;

import DUA.pet.petHouse.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    @JsonManagedReference
    private PagamentoModel pagamento;

    @ManyToMany
    @JoinTable(
            name = "pedido_produto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private Set<ProdutoModel> produtos = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private EnderecoModel endereco;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;


}
