package DUA.petHouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produto_tabela")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProdutoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long id;

    @NotBlank(message = "O nome deve ser valido")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "O produto deve conter uma descrição")
    private String descricao;

    private Double precoVenda;
    private Integer quantidade;

    private String urlFoto;

    @ManyToMany(mappedBy = "produtos")
    @JsonIgnore
    private Set<PedidoModel> itensPedidos = new HashSet<>();

    @ManyToOne
    private CategoriaModel categoria;

    @Column(name = "subcategoria_nome")
    private String subcategoriaNome;

    @Transient
    private Integer quantidadeSelecionada;
}
