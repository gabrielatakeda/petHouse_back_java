package DUA.pet.com.controller;

import DUA.pet.com.model.ProdutoModel;
import DUA.pet.com.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService produtoService;

    @GetMapping("/nome")
    public ResponseEntity<List<ProdutoModel>> findByNome(@RequestParam String nome){
        try {
            return new ResponseEntity<>(produtoService.findByNome(nome), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ProdutoModel> findById(@PathVariable Long id) {
        try {
            var result = produtoService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ProdutoModel>> findAll(){
        try {
            return new ResponseEntity<>(produtoService.findAll(), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ProdutoModel> save(@RequestBody @Valid ProdutoModel produto){
        try {
            return new ResponseEntity<>(produtoService.save(produto), HttpStatus.CREATED);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        try {
            produtoService.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProdutoModel> update(@PathVariable Long id, @RequestBody ProdutoModel produto){
        try {
            var result = produtoService.update(id, produto);
            if(result == null){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
