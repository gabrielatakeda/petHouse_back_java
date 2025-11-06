package DUA.petHouse.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categoria_tb")
@Getter
@Setter
public class CategoriaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @Column(unique = true, nullable = false)
    private String slug;

    @ManyToOne
    @JoinColumn(name = "categoria_pai_id")
    @JsonBackReference
    private CategoriaModel categoriaPai;

    @OneToMany(mappedBy = "categoriaPai", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CategoriaModel> subcategorias;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private List<ProdutoModel> produtos;

    @PrePersist
    @PreUpdate
    public void gerarSlug() {
        if (this.nome != null && !this.nome.isBlank()) {
            this.slug = gerarSlugFormatado(this.nome);
        }
    }

    private String gerarSlugFormatado(String nome) {
        return nome
                .toLowerCase()
                .replaceAll("[áàâã]", "a")
                .replaceAll("[éèê]", "e")
                .replaceAll("[íìî]", "i")
                .replaceAll("[óòôõ]", "o")
                .replaceAll("[úùû]", "u")
                .replaceAll("[ç]", "c")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
