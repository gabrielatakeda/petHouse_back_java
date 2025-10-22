package DUA.petHouse.controller;

import DUA.petHouse.model.CategoriaModel;
import DUA.petHouse.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class  CategoriaController {

    @Autowired
    private final CategoriaService categoriaService;

    @GetMapping("/findAll")
    public ResponseEntity<List<CategoriaModel>> findAll() {
        try{
            var result = categoriaService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            return  new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity< Optional<CategoriaModel>> findById(@PathVariable Long id) {
        try {
            var result = categoriaService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Optional<CategoriaModel>> findByNome(@PathVariable String nome) {
        try {
            var result = categoriaService.findByNome(nome);
            if (result.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<CategoriaModel> save(@RequestBody @Valid CategoriaModel categoria) {
        try {
            System.out.println("Categoria recebida: " + categoria.getNome());
            var result = categoriaService.save(categoria);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/{slugPai}/subcategorias")
    public ResponseEntity<CategoriaModel> criarSubcategoria(
            @PathVariable String slugPai,
            @RequestBody CategoriaModel subcategoria) {

        CategoriaModel novaSubcategoria = categoriaService.criarSubcategoriaPorSlug(slugPai, subcategoria);
        return ResponseEntity.ok(novaSubcategoria);
    }


//    @PutMapping("/update/{id}")
//    public ResponseEntity<CategoriaModel> update(@PathVariable Long id, @RequestBody CategoriaModel categoria){
//        try{
//            var result = categoriaService.update(id,categoria);
//            return new ResponseEntity(result,HttpStatus.OK);
//        } catch (Exception ex) {
//            return  new ResponseEntity(null,HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
//        try {
//            categoriaService.deleteById(id);
//            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
//        } catch (Exception ex) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//
//        }
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
