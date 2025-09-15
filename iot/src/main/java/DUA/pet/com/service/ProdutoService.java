package DUA.pet.com.service;

import DUA.pet.com.model.ProdutoModel;
import DUA.pet.com.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
