package DUA.pet.com.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Campo obrigatorio")
    private Integer qtd;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private PedidoModel pedido;


    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoModel produto;

}
