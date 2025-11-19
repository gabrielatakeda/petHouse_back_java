package DUA.petHouse.controller;

import DUA.petHouse.bucket.BucketFile;
import DUA.petHouse.model.ProdutoModel;
import DUA.petHouse.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProdutoController {
    private final ProdutoService produtoService;

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<ProdutoModel>> findByNome(@PathVariable String nome) {
        try {
            List<ProdutoModel> produtos = produtoService.findByNome(nome);
            if (produtos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(produtos);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoModel> save (
            @RequestPart("produto") ProdutoModel produto,
            @RequestPart("file") MultipartFile file) {

        try (InputStream is = file.getInputStream() ){

            MediaType type = MediaType.parseMediaType(file.getContentType());

            var bucketFile = new BucketFile(file.getName(), is, type, file.getSize());

            ProdutoModel saved = produtoService.save(produto, bucketFile);

            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception ex){
            throw new RuntimeException(ex);
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

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoModel> update(
            @PathVariable Long id,
            @RequestPart("produto") ProdutoModel produto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            BucketFile bucketFile = null;
            if (file != null && !file.isEmpty()) {
                try (InputStream is = file.getInputStream()) {
                    MediaType type = MediaType.parseMediaType(file.getContentType());
                    bucketFile = new BucketFile(file.getOriginalFilename(), is, type, file.getSize());
                }
            }

            ProdutoModel atualizado = produtoService.update(id, produto, bucketFile);
            return ResponseEntity.ok(atualizado);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
