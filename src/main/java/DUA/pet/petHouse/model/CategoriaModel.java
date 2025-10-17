package DUA.pet.petHouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categoria_tabela")
public class CategoriaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String nome;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private List<ProdutoModel> produtos;

    @Column(unique = true, nullable = false)
    private String slug;

    //precisa disso sim pq nao tem definido onde tem as subcategorias estao
    // Categoria pai (self join)
    @ManyToOne
    @JoinColumn(name = "categoria_pai_id")
    private CategoriaModel categoriaPai;

    // Lista de subcategorias
    @OneToMany(mappedBy = "categoriaPai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoriaModel> subcategorias;

}
