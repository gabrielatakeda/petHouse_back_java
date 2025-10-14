package DUA.pet.petHouse.service;

import DUA.pet.petHouse.model.CategoriaModel;
import DUA.pet.petHouse.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public CategoriaModel findById(Long id){
        return categoriaRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public List<CategoriaModel> findAll() {
        return categoriaRepository.findAll();
    }

    public CategoriaModel save(CategoriaModel categoria) {
        // IDENTIFICAR SE CATEGORIA JA EXITE
        // SE SIM LANÇAR EXCEPTION

        Optional<CategoriaModel> categoriaBD = categoriaRepository.findByNome(categoria.getNome());

        return categoriaRepository.save(categoria);
    }

//    public void deleteById(Long id) {
//        var categoria = this.findById(id);
//        categoriaRepository.delete(categoria);
//    }

//    public CategoriaModel update(Long id, CategoriaModel categoria) {
//
//        var obj = this.findById(id);
//
//        if(categoria.getNome() != null && !categoria.getNome().isBlank()){
//            obj.setNome(categoria.getNome());
//        }
//        return  categoriaRepository.save(obj);
//    }

}
