package com.example.iot.service;

import com.example.iot.bucket.BucketFile;
import com.example.iot.bucket.BucketService;
import com.example.iot.model.ProdutoModel;
import com.example.iot.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service //Marca como service, camada de serviço
@RequiredArgsConstructor //Gera os construtores de forma automática
@Slf4j //Gera um log
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final BucketService bucket; //Serviço de armazenamento (para upload de arquivos). Também injetado

    public List<ProdutoModel> findByNome(String nome){ //Metodo para buscar por nome
        return produtoRepository.getByNome(nome);
    }

    public ProdutoModel findById(Long id){ //Metodo para buscar por id
        return produtoRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public List<ProdutoModel> findAll(){ //Metodo para retornar todos os produtos
        return produtoRepository.findAll();
    }

    @Transactional //Garante que todas as operações do metodo sejam executadas juntas
    public ProdutoModel save(ProdutoModel produto, BucketFile bucketFile) { //Metodo que salva um produto e faz upload de uma foto/arquivo associado
        String url = bucket.upload(bucketFile); //Faz upload do arquivo via BucketService e recebe a URL pública/armazenada do arquivo
        produto.setUrlFoto(url); //Define no produto a URL retornada (associa a foto ao produto)
        return produtoRepository.save(produto); //Persiste o produto no banco (insere ou atualiza) e retorna a entidade salva
    }

    public void deleteById(Long id){
        var produto = this.findById(id);
        produtoRepository.delete(produto);
    }

    public ProdutoModel update(Long id, ProdutoModel novoProduto){
        var produto = this.findById(id);
        if(novoProduto.getPrecoVenda() != null){
            produto.setPrecoVenda(novoProduto.getPrecoVenda());
        }
        if(novoProduto.getQuantidade() != null){
            produto.setQuantidade(novoProduto.getQuantidade());
        }

        return produtoRepository.save(produto);
    }
}
