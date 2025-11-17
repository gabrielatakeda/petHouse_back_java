package DUA.petHouse.controller;

import DUA.petHouse.model.CategoriaModel;
import DUA.petHouse.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/save")
    public ResponseEntity<CategoriaModel> save(@RequestBody @Valid CategoriaModel categoria) {
        CategoriaModel result = categoriaService.save(categoria);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // Criar subcategoria usando slug do pai
    @PostMapping("/{slugPai}/subcategoria")
    public ResponseEntity<CategoriaModel> saveSubcategoria(@PathVariable String slugPai,
                                                           @RequestBody @Valid CategoriaModel subcategoria) {
        CategoriaModel result = categoriaService.criarSubcategoriaPorSlug(slugPai, subcategoria);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<CategoriaModel>> findAll() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

//    @GetMapping("/{slug}")
//    public ResponseEntity<CategoriaModel> findBySlug(@PathVariable String slug) {
//        return ResponseEntity.ok(categoriaService.findBySlug(slug));
//    }

    @GetMapping("/pai")
    public ResponseEntity<List<CategoriaModel>> findCategoriasPai() {
        try {
            List<CategoriaModel> categoriasPai = categoriaService.findAll()
                    .stream()
                    .filter(c -> c.getCategoriaPai() == null) // somente categorias sem pai
                    .toList();
            return new ResponseEntity<>(categoriasPai, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/subcategorias")
    public ResponseEntity<List<CategoriaModel>> findSubcategorias(@PathVariable Long id) {
        try {
            Optional<CategoriaModel> categoria = categoriaService.findById(id);
            if (categoria.isPresent()) {
                // Pega diretamente as subcategorias mapeadas na entidade
                List<CategoriaModel> subcategorias = categoria.get().getSubcategorias();
                return new ResponseEntity<>(subcategorias, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(List.of(), HttpStatus.OK);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}


