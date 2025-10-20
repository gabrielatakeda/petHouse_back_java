package DUA.petHouse.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties //Ativa o suporte a propriedades externas
@ConfigurationProperties(prefix = "minio") //Fala que esta classe vai ler valores do application.properties que começam com minio
@Data
public class MinioProps {

    //Atributos
    //Serão preenchidos automaticamente com os valores colocados no properties
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;

}
