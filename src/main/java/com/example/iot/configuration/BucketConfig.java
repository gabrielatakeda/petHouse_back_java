package com.example.iot.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //O Spring vai olhar essa classe quando a aplicação inicia e registrar os beans que ela criar
public class BucketConfig {

    @Autowired //O @Autowired diz para o Spring criar esse objeto para você e colocar aqui automaticamente
    private MinioProps props; //O "props" contém as informações do MinIo (URL, chave de acesso, chave secreta)

    @Bean //Diz para o Spring que esse metodo cria um objeto que será um bean
    public MinioClient bucketClient(){
        return MinioClient.builder()
                .endpoint(props.getUrl()) //Configura o endereço do servidor MinIO
                .credentials(props.getAccessKey(), props.getSecretKey()) //Configura o login (chaves de acesso)
                .build(); //Finaliza e cria o objeto pronto para uso
    }
}
