package DUA.pet.petHouse;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetHouseApplicationTests {

    @LocalServerPort
    private int port;


}
