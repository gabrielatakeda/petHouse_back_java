package DUA.petHouse.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProps {
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
