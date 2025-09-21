package com.example.iot.service;

import com.example.iot.model.EnderecoModel;
import com.example.iot.repository.EnderecoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoModel save(EnderecoModel enderecoModel){
        return enderecoRepository.save(enderecoModel);
    }

    public EnderecoModel findById(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public void delete(Long id){
        enderecoRepository.deleteById(id);
    }


    public EnderecoModel update(Long id, @Valid EnderecoModel enderecoModel) {
        EnderecoModel enderecoUpdate = this.findById(id);

        enderecoUpdate.setLogradouro(enderecoModel.getLogradouro());
        enderecoUpdate.setCidade(enderecoModel.getCidade());
        enderecoUpdate.setCep(enderecoModel.getCep());
        enderecoUpdate.setEstado(enderecoModel.getEstado());
        enderecoUpdate.setNumero(enderecoModel.getNumero());
        enderecoUpdate.setBairro(enderecoModel.getBairro());

        return enderecoRepository.save(enderecoUpdate);
    }


    public List<EnderecoModel> findAll(){
        return enderecoRepository.findAll();
    }

}
