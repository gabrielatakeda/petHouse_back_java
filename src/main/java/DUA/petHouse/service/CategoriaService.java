package DUA.petHouse.service;

import DUA.petHouse.model.CategoriaModel;
import DUA.petHouse.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    @Autowired
    private final CategoriaRepository categoriaRepository;

    public Optional<CategoriaModel> findByNome(String nome){
        return categoriaRepository.findByNome(nome);
    }

    public  Optional<CategoriaModel> findById(Long id){
        return categoriaRepository.findById(id);
    }

    public List<CategoriaModel> findCategoriasPai() {
        return categoriaRepository.findCategoriasPai();
    }


    public List<CategoriaModel> findAll() {
        return categoriaRepository.findAll();
    }

    public CategoriaModel save(CategoriaModel categoria) {
        // IDENTIFICAR SE CATEGORIA JA EXITE
        // SE SIM LANÇAR EXCEPTION
        if (categoria.getSlug() == null || categoria.getSlug().isBlank()) {
            categoria.setSlug(gerarSlug(categoria.getNome()));
        }

        Optional<CategoriaModel> categoriaBD = categoriaRepository.findByNome(categoria.getNome());

        return categoriaRepository.save(categoria);
    }

    public CategoriaModel criarSubcategoriaPorSlug(String slugPai, CategoriaModel subcategoria) {
        CategoriaModel categoriaPai = categoriaRepository.findBySlug(slugPai)
                .orElseThrow(() -> new RuntimeException("Categoria pai não encontrada"));
        subcategoria.setCategoriaPai(categoriaPai);
        return categoriaRepository.save(subcategoria);
    }

    private String gerarSlug(String nome) {
        String normalizado = Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return normalizado.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

}
