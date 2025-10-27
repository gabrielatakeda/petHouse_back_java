package DUA.petHouse.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Erro genérico");
        ResponseEntity<Object> response = handler.handleGenericException(ex);

        assertEquals(500, ((HashMap<String, Object>) response.getBody()).get("status"));
        assertEquals("Erro genérico", ((HashMap<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso não encontrado");
        ResponseEntity<Object> response = handler.handleResourceNotFound(ex);

        assertEquals(404, ((HashMap<String, Object>) response.getBody()).get("status"));
        assertEquals("Recurso não encontrado", ((HashMap<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testHandleConflict() {
        ResourceConflictException ex = new ResourceConflictException("Conflito de recurso");
        ResponseEntity<Object> response = handler.handleConflict(ex);

        assertEquals(409, ((HashMap<String, Object>) response.getBody()).get("status"));
        assertEquals("Conflito de recurso", ((HashMap<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testHandleUnauthorized() {
        UnauthorizedException ex = new UnauthorizedException("Não autorizado");
        ResponseEntity<Object> response = handler.handleUnauthorized(ex);

        assertEquals(401, ((HashMap<String, Object>) response.getBody()).get("status"));
        assertEquals("Não autorizado", ((HashMap<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testHandleMissingParams() {
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("nome", "String");
        ResponseEntity<Object> response = handler.handleMissingParams(ex);

        assertEquals(400, ((HashMap<String, Object>) response.getBody()).get("status"));
        assertEquals("nome é obrigatório", ((HashMap<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testHandleValidationException() {
        // Mock do BindingResult e FieldError
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        var bindingResult = mock(org.springframework.validation.BindingResult.class);
        var fieldError = new org.springframework.validation.FieldError("obj", "campo", "não pode ser nulo");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError));

        ResponseEntity<Object> response = handler.handleValidationException(ex);

        assertEquals(400, ((HashMap<String, Object>) response.getBody()).get("status"));
        assertTrue(((String)((HashMap<String, Object>) response.getBody()).get("message")).contains("campo: não pode ser nulo"));
    }
}
