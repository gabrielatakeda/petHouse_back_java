package com.example.iot.bucket;

import org.springframework.http.MediaType; //É uma classe do Spring para representar o tipo do arquivo (PNG, JPEG etc)

import java.io.InputStream; //É uma forma de ler os bytes do arquivo

//Record serve para representar dados imutáveis de forma bem curta, sem precisar criar uma classe cheia de getters e construtores
public record BucketFile(String name, InputStream is, MediaType type, Long size) {
    /*Name é o nome do arquivo
    is é o conteúdo do arquivo
    type é o tipo do arquivo, por exemplo, PNG
    size é o tamanho do arquivo, em bytes
     */
}
