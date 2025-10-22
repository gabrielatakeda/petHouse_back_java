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
            //Espera uma parte chamada "produto" contendo um JSON (o produto), o Spring converte para ProdutoModel
            @RequestPart("produto") ProdutoModel produto,
            //Espera a parte chamada "file" com o arquivo enviado. É essa parte que será enviada ao MinIO
            @RequestPart("file") MultipartFile file) {

        //Abre um fluxo de leitura do arquivo enviado, permite ler os bytes para enviar ao MinIo
        try (InputStream is = file.getInputStream() ){

            //Pega o tipo do conteúdo do arquivo e transforma em um objeto MediaType
            MediaType type = MediaType.parseMediaType(file.getContentType());

            /*Cria um objeto de transporte com os dados do arquivo (nome, tamanho, conteúdo, tipo),
            esse objeto é passado para o ProdutoService, que vai fazer o upload para o MinIO usando essas informações, depois do upload, o service gera uma url de acesso ao arquivo*/
            var bucketFile = new BucketFile(file.getName(), is, type, file.getSize());

            ProdutoModel saved = produtoService.save(produto, bucketFile);

            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception ex){ //Captura qualquer exceção que possa ocorrer dentro do try acima
            throw new RuntimeException(ex); //A exceção não é mais silenciosa, ela vai “subir” para o Spring, que vai transformá-la em resposta HTTP 500
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
