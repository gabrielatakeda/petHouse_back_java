package com.example.iot.model;

import com.example.iot.enums.MetodoPagamento;
import com.example.iot.enums.Role;
import com.example.iot.enums.StatusPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private PedidoModel pedido;

//    @NotNull
//    @Column(nullable = false, precision = 10, scale = 2)
//    private BigDecimal valor = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    @Column(nullable = false)
    private LocalDate dataPagamento;

//    @Column(unique = true)
//    private String transacaoId;

    @PrePersist
    public void prePersist() {
        this.dataPagamento = LocalDate.now();
        if(this.statusPagamento == null) {
            this.statusPagamento = statusPagamento.PENDENTE;
        }
    }
}
