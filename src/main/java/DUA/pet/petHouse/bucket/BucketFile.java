package DUA.pet.petHouse.bucket;

import org.springframework.http.MediaType;

import java.io.InputStream;

//Record serve para representar dados imutáveis de forma bem curta, sem precisar criar uma classe cheia de getters e construtores
public record BucketFile(String name, InputStream is, MediaType type, Long size) {
    /*Name é o nome do arquivo
    is é o conteúdo do arquivo
    type é o tipo do arquivo, por exemplo, PNG
    size é o tamanho do arquivo, em bytes
     */
}
