package DUA.pet.petHouse.bucket;

import DUA.pet.petHouse.configuration.MinioProps;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BucketService {

    private final MinioClient client; //Objeto que sabe falar com o MinIO
    private final MinioProps props; //Contém as informações do bucket (nome, URL, credenciais)

    //Cria uma lista fixa de tipos de arquivos que são aceitos no upload (JPEG e PNG)
    private static final Set<MediaType> SUPPORTED_TYPES = Set.of(
            MediaType.IMAGE_JPEG,
            MediaType.IMAGE_PNG
    );


    public String upload(BucketFile file){ //Metodo público para enviar um arquivo para o MinIO
        try {
            if (file.type() == null || !SUPPORTED_TYPES.contains(file.type())) { //Verifica se o tipo do arquivo é nulo ou não está na lista de tipos permitidos
                throw new IllegalArgumentException("Apenas imagens PNG ou JPEG são permitidas."); //Lança erro e não envia o arquivo
            }

            //Usa o nome original + um UUID aleatório para evitar sobrescrever arquivos existentes
            var nome = file.name() +  UUID.randomUUID().toString(); //Cria um nome único para o arquivo no bucket

            //Prepara os argumentos para o upload
            var object = PutObjectArgs.builder()
                    .bucket(props.getBucket()) //Diz qual bucket usar
                    .object(nome) //Nome do arquivo dentro do bucket
                    .stream(file.is(), file.size(), -1) //Pega os bytes do arquivo do InputStream
                    .contentType(file.type().toString()) //Informa o tipo do arquivo para o MinIO
                    .build(); //Cria o objeto pronto para ser enviado

            client.putObject(object); //Usa o MinioClient para enviar o arquivo para o bucket
            var url = getUrl(nome); //Gera uma URL para acessar o arquivo (chamando o metodo getUrl)
            return url; //Retorna essa URL
        } catch (Exception ex){ //Se qualquer erro acontecer, lança uma exceção genérica
            throw new RuntimeException(ex);
        }
    }

    public String getUrl(String fileName) { //Monta manualmente a URL para acessar o arquivo
        return String.format("%s/%s/%s", props.getUrl(), props.getBucket(), fileName);
    }

}
