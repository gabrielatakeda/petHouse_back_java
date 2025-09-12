package com.example.iot.model;

import com.example.iot.enums.MetodoPagamento;
import com.example.iot.enums.StatusPagamento;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "pedido_id", nullable = false)
//    private PedidoModel pedido;

    @OneToOne
    @JoinColumn(name = "pedido_id", unique = true)
    @JsonBackReference
    private PedidoModel pedido;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MetodoPagamento metodoPagamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPagamento status;

    @Column(nullable = false)
    private LocalDateTime dataPagamento;

    @Column(unique = true)
    private String transacaoId;

    @PrePersist
    public void prePersist() {
        this.dataPagamento = LocalDateTime.now();
    }
}
