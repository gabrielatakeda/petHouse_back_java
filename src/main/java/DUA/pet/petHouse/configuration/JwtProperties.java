package DUA.pet.petHouse.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    @Value("${jwt.secret}") // injeta o valor de jwt.secret do application.properties
    private String secret;

    @Value("${jwt.expiration-hours}") // injeta jwt.expiration-hours do application.properties
    private int expirationHours;

    public String getSecret() {
        return secret;
    }

    public int getExpirationHours() {
        return expirationHours;
    }
}
