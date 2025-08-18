package com.example.iot.service;

import com.example.iot.model.ProdutoModel;
import com.example.iot.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service //Marca como service, camada de serviço
@RequiredArgsConstructor //Gera os construtores de forma automática
@Slf4j //Gera um log
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

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

    public ProdutoModel save(ProdutoModel produto){
        return produtoRepository.save(produto);
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
