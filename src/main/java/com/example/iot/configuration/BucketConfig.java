package com.example.iot.configuration;

import io.minio.MinioClient; //Importa o MinioClient, que é a classe usada para se conectar ao servidor MinIO (armazenamento de arquivos)
import org.springframework.beans.factory.annotation.Autowired; //Diz para o Spring injetar (preencher) automaticamente o valor de um campo
import org.springframework.context.annotation.Bean; //Esse metodo cria algo que você deve guardar e disponibilizar para o resto da aplicação, no caso o Spring faz
import org.springframework.context.annotation.Configuration; //Essa classe tem metodos que criam beans (objetos que o Spring vai gerenciar)

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
