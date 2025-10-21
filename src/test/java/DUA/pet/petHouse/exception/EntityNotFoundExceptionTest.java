package DUA.pet.petHouse.exception;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        EntityNotFoundException ex = new EntityNotFoundException("Produto não encontrado");
        assertEquals("Produto não encontrado", ex.getMessage());
    }
}
