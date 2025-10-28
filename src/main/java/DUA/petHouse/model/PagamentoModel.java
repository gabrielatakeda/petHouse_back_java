package DUA.petHouse.model;

import DUA.petHouse.enums.MetodoPagamento;
import DUA.petHouse.enums.StatusPagamento;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", unique = true)
    @JsonBackReference
    private PedidoModel pedido;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    @Column(nullable = false)
    private LocalDate dataPagamento;

    @PrePersist
    public void prePersist() {
        this.dataPagamento = LocalDate.now();
        if(this.statusPagamento == null) {
            this.statusPagamento = statusPagamento.PENDENTE;
        }
        if (this.metodoPagamento == null){
            this.metodoPagamento = MetodoPagamento.CARTAO;
        }
    }
}
