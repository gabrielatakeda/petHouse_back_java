package DUA.petHouse.bucket;

import DUA.petHouse.configuration.MinioProps;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BucketServiceTest {

    private MinioClient minioClient;
    private MinioProps minioProps;
    private BucketService bucketService;

    @BeforeEach
    void setUp() {
        minioClient = mock(MinioClient.class);
        minioProps = mock(MinioProps.class);
        when(minioProps.getBucket()).thenReturn("meu-bucket");
        when(minioProps.getUrl()).thenReturn("http://localhost:9000");
        bucketService = new BucketService(minioClient, minioProps);
    }

    @Test
    void upload_deveRetornarUrl_quandoArquivoValido() throws Exception {
        byte[] dados = "conteudo".getBytes();

        BucketFile file = new BucketFile(
                "arquivo",
                new ByteArrayInputStream(dados),
                MediaType.IMAGE_JPEG,
                (long) dados.length
        );

        String url = bucketService.upload(file);

        assertNotNull(url);
        assertTrue(url.contains("http://localhost:9000/meu-bucket/arquivo"));
        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void upload_deveLancarRuntimeException_quandoTipoInvalido() {
        BucketFile file = new BucketFile(
                "arquivo",
                new ByteArrayInputStream(new byte[0]),
                MediaType.APPLICATION_PDF,
                0L
        );

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bucketService.upload(file));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertEquals("Apenas imagens PNG ou JPEG são permitidas.", ex.getCause().getMessage());
        verifyNoInteractions(minioClient);
    }


    @Test
    void upload_deveLancarRuntimeException_quandoErroNoMinio() throws Exception {
        byte[] dados = "conteudo".getBytes();

        BucketFile file = new BucketFile(
                "arquivo",
                new ByteArrayInputStream(dados),
                MediaType.IMAGE_PNG,
                (long) dados.length
        );

        doThrow(new RuntimeException("erro Minio")).when(minioClient).putObject(any(PutObjectArgs.class));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bucketService.upload(file));
        assertTrue(ex.getMessage().contains("erro Minio"));
    }

    @Test
    void getUrl_deveRetornarUrlCorreta() {
        String url = bucketService.getUrl("arquivo.jpg");
        assertEquals("http://localhost:9000/meu-bucket/arquivo.jpg", url);
    }
}
