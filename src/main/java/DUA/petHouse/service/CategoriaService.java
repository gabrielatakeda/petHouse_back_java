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

    public List<CategoriaModel> findAll() {
        return categoriaRepository.findAll();
    }

    public CategoriaModel save(CategoriaModel categoria) {
        // Caso tenha subcategorias no JSON
        if (categoria.getSubcategorias() != null && !categoria.getSubcategorias().isEmpty()) {
            categoria.getSubcategorias().forEach(sub -> sub.setCategoriaPai(categoria));
        }
        return categoriaRepository.save(categoria);
    }

    // Salvar subcategoria usando slug do pai
    public CategoriaModel saveSubcategoriaBySlug(String slugPai, CategoriaModel subcategoria) {
        CategoriaModel pai = categoriaRepository.findBySlug(slugPai)
                .orElseThrow(() -> new RuntimeException("Categoria pai não encontrada pelo slug: " + slugPai));
        subcategoria.setCategoriaPai(pai);
        return categoriaRepository.save(subcategoria);
    }

    public CategoriaModel findBySlug(String slug) {
        return categoriaRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

}                                                                                                               
