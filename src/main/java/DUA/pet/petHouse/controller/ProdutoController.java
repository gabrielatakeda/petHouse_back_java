package DUA.pet.petHouse.controller;

import DUA.pet.petHouse.bucket.BucketFile;
import DUA.pet.petHouse.model.ProdutoModel;
import DUA.pet.petHouse.service.ProdutoService;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //Indica que o endpoint recebe multipart/form-data (arquivo + partes)
    public ResponseEntity<ProdutoModel> save ( //Função de criar um novo produto
            @RequestPart("produto") ProdutoModel produto, //Espera uma parte chamada "produto" contendo um JSON (o produto), o Spring converte para ProdutoModel
            @RequestPart("file") MultipartFile file) { //Espera a parte chamada "file" com o arquivo enviado. É essa parte que será enviada ao MinIO
        try (InputStream is = file.getInputStream() ){ //Abre um fluxo de leitura do arquivo enviado, permite ler os bytes para enviar ao MinIo
            MediaType type = MediaType.parseMediaType(file.getContentType()); //Pega o tipo do conteúdo do arquivo e transforma em um objeto MediaType
            var bucketFile = new BucketFile(file.getName(), is, type, file.getSize()); /*Cria um objeto de transporte com os dados do arquivo (nome, tamanho, conteúdo, tipo),
            esse objeto é passado para o ProdutoService, que vai fazer o upload para o MinIO usando essas informações, depois do upload, o service gera uma url de acesso ao arquivo*/

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
